package view;

import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 * @author Matthieu Carteron
 */
public class KeyTextField extends PlainDocument implements DocumentListener
{
    // Référence sur le label de la clef :
    private final JLabel keyLabel;
    
    // Le nombre de caractère limite :
    private final int limit;

    /**
     * Instantie un champ réservé à l'usage de la clef de cryptage.
     * @param keylabel Référence sur le label de la clef.
     * @param limit Le nombre de caractère limite.
     */
    public KeyTextField(final JLabel keylabel, int limit)
    {
        super();
        
        this.addDocumentListener(this); // On ajoute l'objet lui-même en tant que listener.
        
        this.keyLabel = keylabel;
        this.limit = limit;
    }
    
    // Exécutée lorsque des caractères sont insérés :
    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
    {
        if (str == null)
            return;

        if ((getLength() + str.length()) <= limit)
            super.insertString(offset, str, attr);
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        this.changeString(getLength());
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        this.changeString(getLength());
    }

    // Exécutée lorsque le champ est changé :
    @Override
    public void changedUpdate(DocumentEvent e)
    {
        this.changeString(getLength());
    }
    
    // Change le label de la clef :
    private void changeString(int length)
    {
        this.keyLabel.setText("Clef : " + length + " / 12 caractères (à Bruteforce : " + (12-length) + ") :");
    }
}