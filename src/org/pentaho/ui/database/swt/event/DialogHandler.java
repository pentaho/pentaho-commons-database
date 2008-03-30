package org.pentaho.ui.database.swt.event;

import org.pentaho.ui.xul.impl.XulEventHandler;
import org.pentaho.ui.xul.containers.XulDeck;
import org.pentaho.ui.xul.containers.XulListbox;

/**
 *  Handles functions of the database dialog. 
 *  
 * @author gmoran
 * @created Mar 25, 2008
 */
public class DialogHandler extends XulEventHandler {
  
  private XulListbox deckOptionsBox;
  private XulDeck deck;

  private String packagePath = "org/pentaho/ui/database/";
  
  public DialogHandler() {
  }
  
  public void setDeckChildIndex(){
    
    deckOptionsBox = (XulListbox)document.getElementById("deck-options-list");
    deck = (XulDeck)document.getElementById("dialog-panel-deck");
    int selected = deckOptionsBox.getSelectedIndex();
    deck.setSelectedIndex(selected);
    
  }
  
  @Override
  public Object getData() {
    return null;
  }

  @Override
  public void setData(Object arg0) {
  }


}
