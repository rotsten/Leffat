

package Leffakortisto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InvalidClassException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;


import com.sun.corba.se.impl.io.OptionalDataException;



  /**********************************************************
   * Leffa-projekti:
   *
   * Kirsi Rotsten,
   *
   * huhtikuussa 2010
   * 
   * Kortisto on Leffat-kortiston Controller-luokka,
   * joka hoitaa tiedostoon tallentamisen ja sieltä lukemisen. 
   * Tämä luokka toimii myös käyttöliittymäsovittimena.
   **********************************************************/
public class Kortisto implements Serializable {

  //public static List <Leffa> Leffalista = (LinkedList) Collections.synchronizedList (new LinkedList());
  //public static List Tuloslista = (LinkedList) Collections.synchronizedList (new LinkedList());

  private static final long serialVersionUID = 123456789; 
  public static LinkedList <Leffakortisto.Leffa> Leffalista = new LinkedList();
  public static LinkedList <Leffakortisto.Leffa> Tuloslista = new LinkedList();
  public static String tiedosto = "";
 
  public static Scanner sc = new Scanner(System.in);

  public static int viimTietue(){
    /*
     * Metodi palauttaa leffalistan viimeisen
     * käytössä olevan tietueen numeron.
     */
     return Leffalista.lastIndexOf(Leffalista);
  }
   
  public static String kysyTiedostonnimi() throws IOException {
  
    String l_nimi = null;
    String sopimattomatMerkit = "*,|?:/<>\"";
    sc.nextLine(); // tyhjennä newline puskurista

    try {
      do {
        System.out.println("Anna tiedoston nimi (esim. leffat.txt)");
   	    l_nimi = sc.nextLine();
 	    l_nimi.toLowerCase();

	/* Tiedoston nimen tulee olla ainakin 1 merkin pituinen
	 * (pelkkää enteriä painamalla tästä ei pääse eteenpäin).
	 *
    	 * Lisäksi tiedostonnimi ei saa sisältää erikoismerkkejä,
	 * jotka on lueteltu muuttujassa sopimattomatMerkit.
	 */
        } while ((l_nimi == null ||
                  true == l_nimi.contains(sopimattomatMerkit)));

    } catch (InputMismatchException e) {
      System.out.print("Tarkista antamasi tiedostonnimi");
    }
    return l_nimi;
  }
  
  public static void leffatTalteen(File l_tiedosto) throws IOException {

     System.out.println("Tallettaa leffat tiedostoon... ");
     String l_nimi = "";

     if (l_tiedosto == null) {
       do {
         l_nimi = kysyTiedostonnimi();
         l_nimi.toLowerCase();
       } while (l_nimi == null);
     
       if (l_tiedosto.exists()) {
         System.out.println("Tiedosto "+l_nimi+ " on jo olemassa.");
         System.out.println("Haluatko korvata sen uudella? (K/E)");
         String l_merkkijono = sc.next(); // luetaan syöttörivi
         l_merkkijono.toUpperCase();
         char l_vastaus = l_merkkijono.charAt(0);
				
         if (l_vastaus == 'K' ||
             l_vastaus == 'k') {
    	   kirjoitaTiedostoon(l_tiedosto);
         }
         else {
           System.out.println("Vanha tiedosto säilyy");
         } // end if
       }
       else {
         kirjoitaTiedostoon(l_tiedosto);
       } // end if
     }
     else {
       kirjoitaTiedostoon(l_tiedosto);
     } // end if
  }

  public static void kirjoitaTiedostoon(File l_tiedosto) throws IOException{	
	  
    try {
      /* LeffatTalteen metodi avaa luettavan tiedoston ja jatkaa aiemmin avatun
       * tiedoston kirjoittamista viimeisen kirjoitetun tietueen jälkeen (eli ei
       * ylikirjoita aiemmin kirjoitetun päälle).
       */

      ObjectOutputStream l_output = new ObjectOutputStream (new FileOutputStream(l_tiedosto));

      /* Jos tiedostosta ei ole ensin luettu leffoja Leffalistaan,
       * jota nyt ollaan tallettamassa, aiemmin tiedostoon kirjoitetut
       * leffat menetetään.
       *
       * Korjausehdotuksia?
       * Leffat pitää aina lukea ensin Leffatiedostoon?
       */

      l_output.writeObject(Leffalista);
      
      l_output.flush();
      l_output.close();  
    } catch ( InvalidClassException e ) {
        System.out.println( "Tiedoston lukemisessa törmättiin versio-ongelmaan");
	   /* - The serial version of the class does not match that of the class descriptor read from the stream
	    * - The class contains unknown datatypes
	    * - The class does not have an accessible no-arg constructor
	    */
    } catch ( OptionalDataException e ) {
	System.out.println( "OptionalDataException..." );
    } catch ( FileNotFoundException e ) {
	System.out.println( "Tiedostoa ei löytynyt" );
    } catch ( NotSerializableException e ) {
        System.out.println( "NotSerializableException" );
    } catch ( IOException e ) {
	System.out.println( "Tiedoston kirjoitusvirhe." );
    }
  }
 
  public static void leffatLevylta(File l_tiedosto) throws IOException  {
	
    System.out.println( "Elokuvat luetaan tiedostosta..." );

    //String l_tiedosto = kysyTiedostonnimi();
    //l_tiedosto.toLowerCase();
	  
    try {
      ObjectInputStream l_input = new ObjectInputStream (new FileInputStream(l_tiedosto));
	 
      Leffalista = (LinkedList)l_input.readObject();
      //Leffa.tulostaLeffat(Leffalista, 1);
      l_input.close();
    } catch ( InvalidClassException e ) {
      System.out.println( "InvalidClassException..." );
    } catch ( ClassNotFoundException e ) {
      System.out.println( "Tietueen luku epäonnistui, ClassNotFoundException " );
    } catch ( OptionalDataException e ) {
      System.out.println( "OptionalDataException..." );
    } catch ( FileNotFoundException e ) {
      System.out.println( "Tiedostoa ei löytynyt" );
    } catch ( NotSerializableException e ) {
      System.out.println( "NotSerializableException" );
    } catch ( IOException e ) {
      System.out.println( "Tiedoston lukuvirhe, IOException " );
      System.out.println( "Tarkista, onko tiedosto tyhjä" );
    }
  }

/*************************************************************************
 *  Listaustuloksen tallentamista varten olevat metodit
 *
 *************************************************************************/

  public static void tulosTalteen(String l_listaus) throws IOException {

     System.out.println("Tallettaa listauksen... ");
     String l_nimi = "";

     sc.nextLine(); // tyhjennä newline puskurista

     do {
       l_nimi =  kysyTiedostonnimi();
       l_nimi.toLowerCase();
     } while (l_nimi == null);
     
     File l_tiedosto = new File(l_nimi);

     if (l_tiedosto.exists()) {
       System.out.println("Tiedosto "+l_nimi+ " on jo olemassa.");
       System.out.println("Haluatko korvata sen uudella? (K/E)");
       String l_merkkijono = sc.next(); // luetaan syöttörivi
       l_merkkijono.toUpperCase();
       char l_vastaus = l_merkkijono.charAt(0);

       if (l_vastaus == 'K' ||
           l_vastaus == 'k') {
    	 kirjoitaTulosTiedostoon(l_tiedosto, l_listaus);
       }
       else {
         System.out.println("Vanha tiedosto säilyy");
       } // end if
     }
     else {
       kirjoitaTulosTiedostoon(l_tiedosto, l_listaus);
     } // end if
  }

  public static void kirjoitaTulosTiedostoon(File l_tiedosto, String l_listaus) throws IOException{

    try {

      FileWriter f1 = new FileWriter(l_tiedosto);
      f1.write(l_listaus);
      f1.close();

    } catch ( IOException e ) {
	System.out.println( "Tiedoston kirjoitusvirhe." );
    }
  }

  /*
  public static void main(String[] args) throws IOException {

    // Testausta

      leffatLevylta();
      
      leffatTalteen();
  }*/
}
