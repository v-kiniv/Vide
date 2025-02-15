package de.malban.config.sound;


import de.malban.config.Configuration;
import de.malban.gui.components.CSATableModel;
import de.malban.gui.components.CSATablePanel;
import de.malban.gui.components.DoubleClickAction;
import de.malban.gui.dialogs.InternalFrameFileChoser;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class SoundMapPanel extends javax.swing.JPanel {
    private SoundMap mSoundMap = new SoundMap();
    private SoundMapPool mSoundMapPool;
    private int mClassSetting=0;

    private CSATablePanel csaPanel = null;
    private int mSelectedRow = -1;
    // event gotten row / column of double clicked tableentry
    private int mRow=-1;
    private int mCol=-1;

    Vector<SoundEffect> mAIEffects = new Vector<SoundEffect>();
    SoundEffect mCurrentAIEffect = new SoundEffect();


    /** Creates new form SoundMapPanel */
    public SoundMapPanel() {
        initComponents();
        mSoundMapPool = new SoundMapPool();

        CSATableModel t = CSATableModel.buildTableModel(getTableModel());
        csaPanel = new CSATablePanel(t);
        csaPanel.setXMLId("NamedFXEffectTable");
        csaPanel.setTableStyleSwitchingEnabled(false);
        csaPanel.setTablePopupEnabled(true);
        csaPanel.setDoubleClickAction(new DoubleClickAction()
        {
            @Override
            public void doIt()
            {
                JTable table = (JTable) evt.getSource();
                mRow = table.convertRowIndexToModel(table.rowAtPoint(evt.getPoint()));
                mCol = table.convertColumnIndexToModel(table.columnAtPoint(evt.getPoint()));
                mCol = csaPanel.getModel().convertEnabledColToRealCol(mCol);
                tableDoubleClicked();
            }
        });
        resetConfigPool(false);

        jComboBox4.removeAllItems();
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(SoundEffect.FX_EFFECT_TRIGGERS));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(csaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(csaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
        );


    }
    private void tableDoubleClicked()
    {
        if (mRow <0) return;
        mCurrentAIEffect = SoundEffect.getEffectData(mSoundMap, mRow);
        setEffectData();
    }

    private void resetConfigPool(boolean select)
    {
        mClassSetting++;
        Collection<SoundMap> colC = mSoundMapPool.getHashMap().values();
        Iterator<SoundMap> iterC = colC.iterator();

        jComboBoxName.removeAllItems();
        int i = 0;
        while (iterC.hasNext())
        {
            SoundMap item = iterC.next();
            jComboBoxName.addItem(item.mName);
            if ((i==0) && (select))
            {
                jComboBoxName.setSelectedIndex(0);
                mSoundMap = mSoundMapPool.get(item.mName);
            }
            i++;
        }
        if (!select)  jComboBoxName.setSelectedIndex(-1);
        setAllFromCurrent();
        mClassSetting--;
    }

    private void clearAll() /* allneeded*/
    {
        mClassSetting++;
        mSoundMap = new SoundMap();
        setAllFromCurrent();
        mClassSetting--;
    }

    private void setAllFromCurrent() /* allneeded*/
    {
        mClassSetting++;
        jComboBoxName.setSelectedItem(mSoundMap.mName);
        jTextFieldName.setText(mSoundMap.mName);
        setEffectData();

        mAIEffects = SoundEffect.buildEffectVector(mSoundMap);
        mCurrentAIEffect = new SoundEffect();
        setEffectData();

        csaPanel.reInit();
        mClassSetting--;
    }

    private void readAllToCurrent() /* allneeded*/
    {
        mSoundMap.mName = jTextFieldName.getText();
     
//        jButtonAddFxActionPerformed(null);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBoxName = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jButtonNew = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAsNew = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextFieldSound = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldSound1 = new javax.swing.JTextField();
        jTextFieldSound2 = new javax.swing.JTextField();
        jButtonNewFx = new javax.swing.JButton();
        jButtonAddFx = new javax.swing.JButton();
        jButtonDeleteFx = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jComboBoxName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNameActionPerformed(evt);
            }
        });

        jLabel3.setText("Name");

        jButtonNew.setText("New");
        jButtonNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewActionPerformed(evt);
            }
        });

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonSaveAsNew.setText("Save as new");
        jButtonSaveAsNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAsNewActionPerformed(evt);
            }
        });

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3)
                        .addGap(16, 16, 16)
                        .addComponent(jTextFieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxName, 0, 150, Short.MAX_VALUE)
                        .addGap(73, 73, 73))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonSaveAsNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDelete)
                        .addGap(140, 140, 140)))
                .addGap(210, 210, 210))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBoxName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSave)
                    .addComponent(jButtonSaveAsNew)
                    .addComponent(jButtonNew)
                    .addComponent(jButtonDelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );

        jTextFieldSound.setToolTipText("Image path Info");

        jButton10.setText("...");
        jButton10.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton12.setText("!");
        jButton12.setMargin(new java.awt.Insets(0, 2, 0, 1));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel21.setText("Sound");

        jLabel1.setText("Color");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "W", "U", "G", "R", "B" }));

        jLabel2.setText("Type");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Creature", "Instant", "Sorcery", "Land", "Enchantment", "Artifact" }));

        jLabel4.setText("SubType");

        jLabel5.setText("Event");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Card any", "Card played", "Card to grave", "Card to Library", "Player turn", "Player end", "Player add life", "Player lose life", "Game start", "Game end", " ", " " }));

        jLabel6.setText("ID");

        jTextFieldSound1.setToolTipText("Image path Info");

        jTextFieldSound2.setToolTipText("Image path Info");

        jButtonNewFx.setText("New");
        jButtonNewFx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewFxActionPerformed(evt);
            }
        });

        jButtonAddFx.setText("Add");
        jButtonAddFx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddFxActionPerformed(evt);
            }
        });

        jButtonDeleteFx.setText("Delete selected");
        jButtonDeleteFx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteFxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(22, 22, 22)
                        .addComponent(jTextFieldSound, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12)
                        .addGap(268, 268, 268))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.LEADING, 0, 122, Short.MAX_VALUE)
                            .addComponent(jTextFieldSound1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, 122, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, 0, 174, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jButtonAddFx)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonNewFx)
                                .addGap(51, 51, 51)
                                .addComponent(jButtonDeleteFx)))
                        .addGap(6, 6, 6)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldSound2, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                        .addGap(79, 79, 79))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap(629, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10)
                    .addComponent(jButton12)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldSound2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldSound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAddFx)
                    .addComponent(jButtonDeleteFx)
                    .addComponent(jButtonNewFx))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewActionPerformed
        mClassSetting++;
        mSoundMap = new SoundMap();
        clearAll();
        resetConfigPool(false);
        mClassSetting--;
}//GEN-LAST:event_jButtonNewActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed

        readAllToCurrent();
        mSoundMap.mClass="FXMap";
        mSoundMapPool.put(mSoundMap);
        mSoundMapPool.save();
        mClassSetting++;
        resetConfigPool(true);
        jComboBoxName.setSelectedItem(mSoundMap.mName);
        mClassSetting--;
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonSaveAsNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveAsNewActionPerformed
        mSoundMap = new SoundMap();
        SoundEffect.setEffectVector(mSoundMap, mAIEffects);
//        jButtonAddFxActionPerformed(null);
        readAllToCurrent();
        mSoundMap.mClass="FXMap";
        mSoundMapPool.putAsNew(mSoundMap);
        mSoundMapPool.save();
        mClassSetting++;
        resetConfigPool(true);
        jComboBoxName.setSelectedItem(mSoundMap.mName);
        mClassSetting--;
    }//GEN-LAST:event_jButtonSaveAsNewActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        readAllToCurrent();
        mSoundMapPool.remove(mSoundMap);
        mSoundMapPool.save();
        mClassSetting++;
        resetConfigPool(true);

        if (jComboBoxName.getSelectedIndex() == -1)
        {
            clearAll();
        }

        String key = jComboBoxName.getSelectedItem().toString();
        mSoundMap = mSoundMapPool.get(key);
        setAllFromCurrent();

        mClassSetting--;
}//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jComboBoxNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxNameActionPerformed
        if (mClassSetting > 0 ) return;
        String key = jComboBoxName.getSelectedItem().toString();
        mSoundMap = mSoundMapPool.get(key);
        setAllFromCurrent();
    }//GEN-LAST:event_jComboBoxNameActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton10ActionPerformed
    {//GEN-HEADEREND:event_jButton10ActionPerformed

        InternalFrameFileChoser fc = new de.malban.gui.dialogs.InternalFrameFileChoser();
        fc.setCurrentDirectory(new java.io.File("sound"));

        int r = fc.showOpenDialog(Configuration.getConfiguration().getMainFrame());
        if (r != InternalFrameFileChoser.APPROVE_OPTION) return;
        String fullPath = fc.getSelectedFile().getAbsolutePath();
        jTextFieldSound.setText(de.malban.util.UtilityString.makeRelative(fullPath));
}//GEN-LAST:event_jButton10ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton12ActionPerformed
    {//GEN-HEADEREND:event_jButton12ActionPerformed
        String path = jTextFieldSound.getText();
        if (path.length() == 0) return;
        try
        {
            de.malban.sound.Audio.play(de.malban.util.UtilityString.cleanFileString(path));
        }
        catch (Throwable e)
        {}
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButtonAddFxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonAddFxActionPerformed
    {//GEN-HEADEREND:event_jButtonAddFxActionPerformed
        readEffectData();
        if (mCurrentAIEffect.mEvent.trim().length()==0) return;
        SoundEffect.addEffect(mSoundMap, mCurrentAIEffect);
        mAIEffects = SoundEffect.buildEffectVector(mSoundMap);
        csaPanel.reInit();

    }//GEN-LAST:event_jButtonAddFxActionPerformed

    private void jButtonDeleteFxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonDeleteFxActionPerformed
    {//GEN-HEADEREND:event_jButtonDeleteFxActionPerformed

        int[] rows = csaPanel.getTable().getSelectedRows();
        if(rows.length==0) return;

        Vector<Integer> toDeleteIDs = new Vector<Integer>();
        for (int i=rows.length-1; i>=0 ; i--)
        {
            int sel = csaPanel.convertRowIndexToModel(rows[i]);
            SoundEffect.removeEffect(mSoundMap, sel);
        }
        csaPanel.deleteSelected();
        mAIEffects = SoundEffect.buildEffectVector(mSoundMap);
        csaPanel.reInit();
        mCurrentAIEffect = new SoundEffect();
        setEffectData();
    }//GEN-LAST:event_jButtonDeleteFxActionPerformed

    private void jButtonNewFxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonNewFxActionPerformed
    {//GEN-HEADEREND:event_jButtonNewFxActionPerformed
        mCurrentAIEffect = new SoundEffect();
        setEffectData();

    }//GEN-LAST:event_jButtonNewFxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButtonAddFx;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonDeleteFx;
    private javax.swing.JButton jButtonNew;
    private javax.swing.JButton jButtonNewFx;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSaveAsNew;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBoxName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldSound;
    private javax.swing.JTextField jTextFieldSound1;
    private javax.swing.JTextField jTextFieldSound2;
    // End of variables declaration//GEN-END:variables

    private void setEffectData()
    {
        jTextFieldSound.setText(mCurrentAIEffect.mSoundFile);
        jComboBox1.setSelectedItem(mCurrentAIEffect.mColor);
        jComboBox2.setSelectedItem(mCurrentAIEffect.mType);
        jTextFieldSound2.setText(mCurrentAIEffect.mSubtype);
        jTextFieldSound1.setText(mCurrentAIEffect.mID);
        jComboBox4.setSelectedItem(mCurrentAIEffect.mEvent);
    }
    private void readEffectData()
    {
        mCurrentAIEffect.mSoundFile = jTextFieldSound.getText().trim();
        if (jComboBox1.getSelectedIndex() == -1)
            mCurrentAIEffect.mColor = "";
        else
            mCurrentAIEffect.mColor = jComboBox1.getSelectedItem().toString().trim();

        if (jComboBox2.getSelectedIndex() == -1)
            mCurrentAIEffect.mType = "";
        else
            mCurrentAIEffect.mType = jComboBox2.getSelectedItem().toString().trim();

        mCurrentAIEffect.mSubtype = jTextFieldSound2.getText().trim();
        mCurrentAIEffect.mID = jTextFieldSound1.getText().trim();

        if (jComboBox4.getSelectedIndex() == -1)
            mCurrentAIEffect.mEvent = "";
        else
            mCurrentAIEffect.mEvent = jComboBox4.getSelectedItem().toString().trim();
    }

    private javax.swing.table.TableModel getTableModel()
    {
        AbstractTableModel model = new
         AbstractTableModel() {
            @Override public String getColumnName(int col)
            {
                if (col==0) return "Event";
                if (col==1) return "File";
                if (col==2) return "Color";
                if (col==3) return "Type";
                if (col==4) return "Subtype";
                if (col==5) return "ID";
                return "";
            }
            @Override
            public int getRowCount()
            {
                return mAIEffects.size();
            }
            @Override
            public int getColumnCount() { return 6;}
            @Override
            public Object getValueAt(int row, int col)
            {
                if (col==0) return mAIEffects.elementAt(row).mEvent;
                if (col==1) return mAIEffects.elementAt(row).mSoundFile;
                if (col==2) return mAIEffects.elementAt(row).mColor;
                if (col==3) return mAIEffects.elementAt(row).mType;
                if (col==4) return mAIEffects.elementAt(row).mSubtype;
                if (col==5) return mAIEffects.elementAt(row).mID;
                return "";
            }
            @Override public boolean isCellEditable(int row, int col)
                { return false; }
            @Override public Class getColumnClass(int col)
            {
                return String.class;
            }
        };
        return model;
    }
}
