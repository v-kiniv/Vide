// Placed in public domain by William J. Yakowenko, 2002.  Share and enjoy!
//
// This program is provided "as-is", with no warranty of any kind.
// In no event shall the author or any contributor to this program
// be held liable for any damages arising in any way from any action
// related to this program.


// Signifies a problem because of the unexpected non-existance of an item.
package de.malban.vide.assy.exceptions;

public class DoesNotExistException extends Exception {
	String itemName;

	public DoesNotExistException() { }
	public DoesNotExistException( String s ) { super(s); }
	public DoesNotExistException( String s, String n ) {
		super(s);
		itemName = n;
	}

	public String getItemName() { return itemName; }
}

