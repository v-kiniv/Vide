/*

            pi = new PatternInfo();
            pi.name = "Draw_Sync";
            pi.line1Pattern = "";
            pi.lineXPattern = "%S %SY %SX";
            pi.lastLinePattern = "%2";
            kp.add(pi);

* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.malban.vide.veccy;

import de.malban.config.Configuration;
import de.malban.graphics.GFXVector;
import de.malban.graphics.GFXVectorAnimation;
import de.malban.graphics.GFXVectorList;
import de.malban.gui.panels.LogPanel;
import static de.malban.gui.panels.LogPanel.WARN;
import de.malban.vide.dissy.DASM6809;
import de.malban.vide.veccy.VeccyPanel.PatternInfo;
import java.util.ArrayList;

/**
 *
 * @author malban
 */
public class VeccyInterpreter {
    LogPanel log = (LogPanel) Configuration.getConfiguration().getDebugEntity();
    /*
        Known interpretations:
        %R"SOMETHING"
        %C0 count +0[can only be in first line!]
        %C+ count +1[can only be in first line!]
        %C- count -1[can only be in first line!]
        %SX xcoordinate (relative/or absolut)
        %SY xcoordinate (relative/or absolut)
        %X xcoordinate (relative)
        %Y xcoordinate (relative)
        %X0 xcoordinate ()
        %Y0 xcoordinate ()
        %X1 xcoordinate ()
        %Y1 xcoordinate ()

        %CX- xcoordinate (continued dot sequence absolut X) -127 MOVE
        %CY- xcoordinate (continued dot sequence absolut Y) -127 MOVE

        %CX1 xcoordinate (continued dot sequence absolut X) -1 MOVE
        %CY1 xcoordinate (continued dot sequence absolut Y) -1 MOVE

        %P pattern
        %S sync list pattern
        %B brightness (=intensity)
        %M mode
        not possible %S sync mode
        %I ignore (no output)
    
    Breaks for last line
        %0 0
        %1 1
        %- >128
        %+ <128
        %2 ==2
    */
    String line1Pattern=""; // can only be %CX or nothing!
    String lineXPattern=""; // one line must represent ONE vector definition!
    String lastLinePattern=""; // a break option
    
    String[] patternLine1= new String[0];
    String[] patternLineX= new String[0];
    String[] patternLineLast= new String[0];
    
    ArrayList<String> removers = new ArrayList<String>();
    
    String representation = "";
    ArrayList<Byte> dataInterpreted = new ArrayList<Byte>();
    String textOrg="";

    
    public VeccyInterpreter()
    {
        
    }
    
    
    public ArrayList<Byte> setData(String text)
    {
        ArrayList<Byte> data = dataInterpreted;
        
        // windows text to "usable" text
        text = de.malban.util.UtilityString.replace(text, "\r\n","\n");
        String[] textSplits = text.split("\n");

        // search for line comments, and delete them
        StringBuilder newText = new StringBuilder();
        for (String s : textSplits)
        {
            if (s.trim().startsWith("*")) continue;
            s = de.malban.vide.assy.Comment.removeEndOfLineComment(s);
            s = de.malban.vide.assy.Comment.removeCEndOfLineComment(s); // //...
            s = de.malban.vide.assy.Comment.removeC1EndOfLineComment(s); // /*...
            if (s.trim().length()==0) continue;
            newText.append(s+"\n");
        }
        text = newText.toString().toUpperCase();
        
        
        // replace all separators and whitestrngs with "clean" spaces
        for (String remove: removers)
        {
            text = de.malban.util.UtilityString.replace(text, remove, " ");
        }
        text = de.malban.util.UtilityString.replaceWhiteSpaces(text.toUpperCase(), " ");
        text = de.malban.util.UtilityString.replace(text, ";", " ");
        text = de.malban.util.UtilityString.replace(text, ",", " ");
        text = de.malban.util.UtilityString.replace(text, ":", " ");
        text = de.malban.util.UtilityString.replace(text, "*", " ");
        text = de.malban.util.UtilityString.replace(text, "/", " ");
        text = de.malban.util.UtilityString.replace(text, "(", " ");
        text = de.malban.util.UtilityString.replace(text, ")", " ");
        text = de.malban.util.UtilityString.replace(text, "{", " ");
        text = de.malban.util.UtilityString.replace(text, "}", " ");
        text = de.malban.util.UtilityString.replace(text, "[", " ");
        text = de.malban.util.UtilityString.replace(text, "]", " ");
        text = de.malban.util.UtilityString.replace(text, "<", " ");
        text = de.malban.util.UtilityString.replace(text, ">", " ");
        
        
        
        while (true)
        {
            String orgText = text;
            text = de.malban.util.UtilityString.replace(text, "  ", " ");
            if (orgText.equals(text)) break;
        }
        text = de.malban.util.UtilityString.replace(text, "- ", "-");
        text = de.malban.util.UtilityString.replace(text, "+ ", "+");

        // more or less text is now "one" line, with all separations turned to spaces
        String[] splits = text.split(" ");
        
        // now try every entity if it is a number,
        // if so add it to data!
        for (String s : splits)
        {
            int number = toNumber(s.trim());
            if (number == Integer.MAX_VALUE) continue;
            
            
            if ((number <-128) || (number > 255)) // testing signed and unsigned byte in one go... might look strange...
            {
                // assuming word pointer
                data.add((byte) (number>>8 & 0xff));
                data.add((byte) (number & 0xff));
            }
            else
                data.add((byte) (number & 0xff));
        }
        
        return data;
    }
    public void setPatterns(String l1, String lx, String ll)
    {
        String l1Org = l1;
        // remove whitespaces and spaces
        l1 = de.malban.util.UtilityString.replaceWhiteSpaces(l1.toUpperCase(), " ");
        l1 = de.malban.util.UtilityString.replace(l1, ",", "");
        l1 = de.malban.util.UtilityString.replace(l1, ";", "");
        l1 = de.malban.util.UtilityString.replace(l1, ":", "");
        l1 = de.malban.util.UtilityString.replace(l1, " ", "");
        lx = de.malban.util.UtilityString.replaceWhiteSpaces(lx.toUpperCase(), " ");
        lx = de.malban.util.UtilityString.replace(lx, ",", "");
        lx = de.malban.util.UtilityString.replace(lx, ";", "");
        lx = de.malban.util.UtilityString.replace(lx, ":", "");
        lx = de.malban.util.UtilityString.replace(lx, " ", "");
        ll = de.malban.util.UtilityString.replaceWhiteSpaces(ll.toUpperCase(), " ");
        ll = de.malban.util.UtilityString.replace(ll, ",", "");
        ll = de.malban.util.UtilityString.replace(ll, ";", "");
        ll = de.malban.util.UtilityString.replace(ll, ":", "");
        ll = de.malban.util.UtilityString.replace(ll, " ", "");
        
        
        line1Pattern=l1;
        lineXPattern=lx; 
        lastLinePattern=ll;
        
        
        patternLineX = removeEmpty(lineXPattern.split("%"));
        patternLine1 = removeEmpty(line1Pattern.split("%"));
        patternLineLast = removeEmpty(lastLinePattern.split("%"));
        
        ArrayList<String> ss = new ArrayList<String>();

        for (int i=0;i<patternLine1.length; i++)
        {
            String s = patternLine1[i];
            if (s.startsWith("R\""))
            {
                s = s.substring(2, s.length()-1);
            }
            else
            {
                ss.add(s);
            }
        }
        patternLine1 = ss.toArray(new String[0]);

        removers.clear();
        l1Org = de.malban.util.UtilityString.replace(l1Org, "%r\"", "%R\"");
        String[] removersSplit = l1Org.toUpperCase().split("%R\"");
        if (removersSplit.length>1)
        {
            for (String toRemove: removersSplit)
            {
                String remove = toRemove;
                if (remove.length()==0) continue;
                remove = remove.substring(0, remove.length()-1);
                removers.add(remove);
            }
        }
        if (textOrg.length()!=0) setData(textOrg);
    }
    String[] removeEmpty(String[] s)
    {
        ArrayList<String> ss = new ArrayList<String>();
        for (String _s: s)
        {
            if (_s.trim().length()!=0) ss.add(_s);
        }
        return ss.toArray(new String[0]);
    }
    // returns Integer.MAX_VALUE if no number
    public static int toNumber(String s)
    {
        s = s.toUpperCase();
        boolean minus = false;
        int radix = 10;
        int result = 0;
        if (s.startsWith("-"))
        {
            minus=true;
            s = s.substring(1);
        }
        if (s.startsWith("+"))
        {
            s = s.substring(1);
        }
        if (s.startsWith("$"))
        {
            radix = 16;
            s = s.substring(1);
        }
        if (s.startsWith("0X"))
        {
            s= s.substring(2);
            radix = 16;
        }
        try
        {
            result = Integer.parseInt(s, radix);
            if (minus) result *=-1;
        }
        catch (Throwable ex)
        {
            return Integer.MAX_VALUE;
        }
        return result;
    }

    
    class IntegerPointer
    {
        int pos;
    }
    
    public GFXVectorList getVectorList(IntegerPointer startPos)
    {
        ArrayList<Byte> data = dataInterpreted;
        
        StringBuilder s = new StringBuilder();
        int pos = startPos.pos;
        GFXVectorList vl = new GFXVectorList();
        int count = -1; // no count
        if (data.size() == 0) return vl;
        if (pos>= data.size()) return vl;
        
        
        for (String p : patternLine1)
        {
            if (pos>= data.size()) 
            {
                s.append("end of data encountered line 1");
                representation = s.toString();
                startPos.pos = pos;
                return vl;        
            }
            
            if (p.equals("C0"))
            {
                count = data.get(pos++);
                s.append(hex(count));
            }
            else if (p.equals("C+"))
            {
                count = data.get(pos++)+1;
                s.append(hex(count));
            }
            else if (p.equals("C-"))
            {
                count = data.get(pos++)-1;
                s.append(hex(count));
            }            
            else if (p.equals("I")) // ignore
            {
                pos++;
            }
            else if (p.equals("Y"))
            {
                GFXVector v;
                if (vl.size()==0)
                    v = new GFXVector();
                else
                    v = vl.get(0);
                v.pattern = 0;
                s.append(hex(data.get(pos)));
                int yval = data.get(pos++)&0xff;
                if (yval>127)yval-=256;
                
                v.end.y(yval);
                vl.remove(v);
                vl.add(v);
            }
            else if (p.equals("X"))
            {
                GFXVector v;
                if (vl.size()==0)
                    v = new GFXVector();
                else
                    v = vl.get(0);
                v.pattern = 0;
                s.append(hex(data.get(pos)));
                
                int xval = data.get(pos++)&0xff;
                if (xval>127)xval-=256;
                
                v.end.x(xval);
                
                
                vl.remove(v);
                vl.add(v);
            }
            s.append(" ");
        }        
        if ((patternLine1.length > 0) && (s.length()>0))
            s.append("\n");
        
        boolean syncMode = false;
        int lastAbsX = 0;
        int lastAbsY = 0;
        int saveLastAbsY = 0;
        int saveLastAbsX = 0;
        boolean startingX = true;
        boolean startingY = true;
        int hasNextPattern = 0;
        int nextPattern = 255;
        while (true)
        {
            boolean breaking=false;
            boolean relative = false;
            boolean finished = false;
            boolean dontAdd = false;
            GFXVector v = new GFXVector();
            if (hasNextPattern==2)
            {
                v.pattern = nextPattern;
            }
            hasNextPattern = 0;
            for (String p : patternLineX)
            {
                if (pos>=data.size())
                {
                    breaking = true;
                    break;
                }
                if (pos>= data.size()) 
                {
                    s.append("end of data encountered line x");
                    representation = s.toString();
                    startPos.pos = pos;
                    return vl;        
                }

                // is first entry, than check if stop criteria
                // but only if not FIRST entry
                if (vl.size()!=0)
                {
                    if (p.equals(patternLineX[0]))
                    {
                        int b = data.get(pos);
                        b = b & 0xff;
                        if (lastLinePattern.contains("%0"))
                            if (b == 0) finished = true;
                        if (lastLinePattern.contains("%1"))
                            if (b == 1) finished = true;
                        if (lastLinePattern.contains("%-"))
                            if (b >= 128) finished = true;
                        if (lastLinePattern.contains("%+"))
                            if ((b < 128) && (b>0)) finished = true;
                        if (lastLinePattern.contains("%2"))
                            if (b == 2) finished = true;
                        if (finished) 
                        {
                            s.append(hex(b)+"\n");
                            break;
                        }
                    }
                }
                if (p.equals("Y"))
                {
                    relative = true;
                    s.append(hex(data.get(pos)));
                    int yval = data.get(pos++)&0xff;
                    if (yval>127)yval-=256;
                    v.end.y(yval);
                }
                else if (p.equals("X"))
                {
                    relative = true;
                    s.append(hex(data.get(pos)));

                    int xval = data.get(pos++)&0xff;
                    if (xval>127)xval-=256;
                    v.end.x(xval);
                }
                else if (p.equals("CX-"))
                {
                    if (startingX)
                    {
                        startingX = false;
                        s.append(hex(data.get(pos))+" ");
                        lastAbsX = data.get(pos++);
                        dontAdd = true;
                        continue;
                    }
                    else
                    {
                        s.append(hex(data.get(pos)));
                        if (data.get(pos) == -127)
                        {
                            hasNextPattern++;
                            nextPattern = 0;
                            saveLastAbsX = lastAbsX;
                        }
                        v.start.x(lastAbsX);
                        lastAbsX = data.get(pos++);
                        v.end.x(lastAbsX);
                    }
                }
                else if (p.equals("CY-"))
                {
                    if (startingY)
                    {
                        startingY = false;
                        s.append(hex(data.get(pos)));
                        lastAbsY = data.get(pos++);
                        dontAdd = true;
                        continue;
                    }
                    else
                    {
                        if (data.get(pos) == -127)
                        {
                            hasNextPattern++;
                            nextPattern = 0;
                            saveLastAbsY = lastAbsY;
                        }
                        s.append(hex(data.get(pos)));
                        v.start.y(lastAbsY);
                        lastAbsY = data.get(pos++);
                        v.end.y(lastAbsY);
                    }
                }
                else if (p.equals("CX1"))
                {
                    if (startingX)
                    {
                        startingX = false;
                        s.append(hex(data.get(pos))+" ");
                        lastAbsX = data.get(pos++);
                        dontAdd = true;
                        continue;
                    }
                    else
                    {
                        s.append(hex(data.get(pos)));
                        if (data.get(pos) == -1)
                        {
                            hasNextPattern++;
                            nextPattern = 0;
                            saveLastAbsX = lastAbsX;
                        }
                        v.start.x(lastAbsX);
                        lastAbsX = data.get(pos++);
                        v.end.x(lastAbsX);
                    }
                }
                else if (p.equals("CY1"))
                {
                    if (startingY)
                    {
                        startingY = false;
                        s.append(hex(data.get(pos)));
                        lastAbsY = data.get(pos++);
                        dontAdd = true;
                        continue;
                    }
                    else
                    {
                        if (data.get(pos) == -1)
                        {
                            hasNextPattern++;
                            nextPattern = 0;
                            saveLastAbsY = lastAbsY;
                        }
                        s.append(hex(data.get(pos)));
                        v.start.y(lastAbsY);
                        lastAbsY = data.get(pos++);
                        v.end.y(lastAbsY);
                    }
                }
                
                
                else if (p.equals("SY"))
                {
                    if (!syncMode)
                    {
                        relative = true;
                        s.append(hex(data.get(pos)));
                        int yval = data.get(pos++)&0xff;
                        if (yval>127)yval-=256;
                        v.end.y(yval);
                    }
                    else
                    {
                        s.append(hex(data.get(pos)));
                        v.end.y(data.get(pos++));
                    }
                }
                else if (p.equals("SX"))
                {
                    if (!syncMode)
                    {
                        relative = true;
                        s.append(hex(data.get(pos)));
                        int xval = data.get(pos++)&0xff;
                        if (xval>127)xval-=256;
                        v.end.x(xval);
                    }
                    else
                    {
                        s.append(hex(data.get(pos)));
                        v.end.x(data.get(pos++));
                    }
                }
                else if (p.equals("Y0"))
                {
                    s.append(hex(data.get(pos)));
                    v.start.y(data.get(pos++));
                }
                else if (p.equals("X0"))
                {
                    s.append(hex(data.get(pos)));
                    v.start.x(data.get(pos++));
                }
                else if (p.equals("Y1"))
                {
                    s.append(hex(data.get(pos)));
                    v.end.y(data.get(pos++));
                }
                else if (p.equals("X1"))
                {
                    s.append(hex(data.get(pos)));
                    v.end.x(data.get(pos++));
                }
                else if (p.equals("P"))
                {
                    s.append(hex(data.get(pos)));
                    v.pattern = data.get(pos++)&0xff;
                }
                else if (p.equals("B"))
                {
                    s.append(hex(data.get(pos)));
                    v.intensity = data.get(pos++)&0xff;
                }
                else if (p.equals("I")) // ignore
                {
                    pos++;
                }
                else if (p.equals("M")) 
                {
                    int mode = data.get(pos++)&0xff;
                    s.append(hex(mode));
                    if (mode == 0)
                        v.pattern = 0;
                    else if (mode > 1)
                        v.pattern = 255;
                    else if (mode < 0) // use pattern from memory, we don't have that...
                        v.pattern = 0xaa;
                    else if (mode == 1)
                    {
                        finished = true;
                        s.append("\n");
                        break;
                    }
                }
				
                else if (p.equals("S"))
                {
                    int sPattern = data.get(pos++)&0xff;
                    if (sPattern != 1) // same as "P"
                    {
                        s.append(hex(sPattern));
                        v.pattern = sPattern;
                        syncMode = false;
                    }
                    else
                    {
                        syncMode = true;
                        // sync, that means a vector with absolut 0,0 as start and the given coordinates as end!
                        s.append(hex(sPattern)+" ");
                        v.pattern = 0; // move
                        s.append(hex(0)+" ");
                        v.start.y(0);
                        s.append(hex(0));
                        v.start.x(0);
                    }
                }
				
				
                
                s.append(" ");
            }
            s.append("\n");
            if (finished)
                break;
            if (breaking) 
            {
                log.addLog("Vector interpreter, input out of data!", WARN);
                break;
            }
            
            if (relative)
            {
                if (vl.size() != 0)
                {
                    v.setRelativ(relative);
                    v.start = vl.get(vl.size()-1).end;
                    v.start_connect = vl.get(vl.size()-1);
                    v.uid_start_connect = vl.get(vl.size()-1).uid;
                    vl.get(vl.size()-1).end_connect = v;
                    vl.get(vl.size()-1).uid_end_connect = v.uid;
                    v.end.x(v.end.x() + v.start.x());
                    v.end.y(v.end.y() + v.start.y());
                }
            }
            if (hasNextPattern == 2)
            {
                lastAbsY = saveLastAbsY;
                lastAbsX = saveLastAbsX;
                dontAdd = true;
            }
            
            if (!dontAdd)
            {
                vl.add(v);
            }
            else
            {
                dontAdd = false;
            }
            count--;
            if (count == -1) break; // if no count was available, this break never takes of! This is intended!
            if (pos>=data.size()) break;
        }
        representation = s.toString();
        startPos.pos = pos;
        return vl;        
    }
    
    public GFXVectorList getVectorList()
    {
        IntegerPointer pos = new IntegerPointer();
        pos.pos = 0;
        return getVectorList(pos);
    }
    
    public GFXVectorAnimation getVectorAnimation()
    {
        GFXVectorAnimation al = new GFXVectorAnimation();
        IntegerPointer pos = new IntegerPointer();
        pos.pos = 0;
        int oldPos = -1;
        String allIntRep = "";
        while (oldPos != pos.pos)
        {
            oldPos = pos.pos;
            GFXVectorList vl = getVectorList(pos);
            if (oldPos != pos.pos)
            {
                al.list.add(vl);
                if (allIntRep.length() != 0)
                {
                    allIntRep += ";;;; \n";
                }
                allIntRep += representation;
                
            }
        }
        
        representation = allIntRep;
        return al;
    }
    
    public String toString()
    {
        return representation;
    }
    String hex(int b)
    {
        String s="";
        int idata = b;
        idata = idata & 0xff;
        if (idata>=128)
        {
            idata -= 256;
            idata *= -1;
            s+="-";
        }
        else
        {
            s+="+";
        }
        s+="$";
        s+=String.format("%02X",idata);
        return s;
    }
    
    
    //VList 0: 1: %Y %X %M 2:
    public static VeccyPanel.PatternInfo buildPatternInfo(String comment)
    {
        if (!comment.toUpperCase().contains("VLIST")) return null;
        if (!comment.toUpperCase().contains("0:")) return null;
        if (!comment.toUpperCase().contains("1:")) return null;
        if (!comment.toUpperCase().contains("2:")) return null;

        String p0 = comment.substring(comment.indexOf("0:"+2));
        String p1 = comment.substring(comment.indexOf("1:"+2));
        String p2 = comment.substring(comment.indexOf("2:"+2));
        
        p0 = p0.substring(0,comment.indexOf("1:"+2)).trim();
        p1 = p1.substring(0,comment.indexOf("2:"+2)).trim();
        p2 = p2.trim();
        
        
        PatternInfo pattern = new PatternInfo();
        pattern.name = "noName";
        pattern.line1Pattern = p0;
        pattern.lineXPattern = p1;
        pattern.lastLinePattern = p2;
        return pattern;
    }
    
}
