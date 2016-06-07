package com.example.jfransen44.recycleit;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class RecycleGuide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_guide);
        ImageView eWasteImage = (ImageView) findViewById(R.id.eWasteImage);
        eWasteImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ewaste));
        TextView eWasteText = (TextView) findViewById(R.id.eWasteText);
        eWasteText.setText("E-waste is the popular, informal name for electronic products. Unwanted electronic devices should be reused or recycled. Electronic equipment should NOT be disposed of with regular garbage; in fact, this is illegal in most states. Functioning electronics can be sold or donated thereby prolonging their useful life. Nonfunctioning electronics that cannot be repaired should be recycled by an organization qualified to do so.\n" +
                "-Computers\n" +
                "-Monitors\n" +
                "-Televisions\n" +
                "-Mobile Phones");

        ImageView glassImage = (ImageView) findViewById(R.id.glassImage);
        glassImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.glass));
        TextView glassText = (TextView) findViewById(R.id.glassText);
        glassText.setText("Occasionally a center will accept only certain \"colors\" of glass. Light bulbs, Pyrex, ceramics, and mirrors are NEVER accepted. In some places, glass is becoming less accepted because of the potential of worker injuries due to broken glass.\n" +
                "-Beer bottles\n" +
                "-Wine bottles\n" +
                "-Pickle jars\n" +
                "-Jelly jars");

        ImageView hhwImage = (ImageView) findViewById(R.id.hhwImage);
        hhwImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.hhw));
        TextView hhwText = (TextView) findViewById(R.id.hhwText);
        hhwText.setText("Many local government agencies run programs that help households and small businesses recycle or properly dispose of their hazardous wastes. Additionally, some retailers will collect certain universal wastes, such as batteries.\n" +
                "-Motor oil, oil filters, brake fluid\n" +
                "-Used antifreeze\n" +
                "-Paint, paint thinner, turpentine\n" +
                "-Cleaners with acid or lye\n" +
                "-Household batteries or car batteries\n" +
                "-Pool chemicals\n" +
                "-Fluorescent light bulbs\n" +
                "-Used needles or sharps\n" +
                "-Unwanted or expired prescriptions");

        ImageView metalImage = (ImageView) findViewById(R.id.metalImage);
        metalImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.metal));
        TextView metalText = (TextView) findViewById(R.id.metalText);
        metalText.setText("Aluminum Cans\n" +
                "\n" +
                "-You all know these, the Coke, Pepsi, and Sprites of the world.\n" +
                "-Steel and Tin Cans\n" +
                "\n" +
                "-Soup cans\n" +
                "-Veggie cans\n" +
                "-Coffee cans\n" +
                "* Include the can lids, and you don't even need to remove the labels.");

        ImageView plasticImage = (ImageView) findViewById(R.id.plasticImage);
        plasticImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plastic));
        TextView plasticText = (TextView) findViewById(R.id.plasticText);
        plasticText.setText("Most centers will accept those plastics labeled with the PETE 1 and HDPE 2 symbols and many more are accepting plastics labeled 1 - 7. These can generally be found molded into the bottom of the containers. If you can't identify the type of plastic, don't include it. Most facilities also require that you remove the cap since it is usually made of a different type of plastic.\n" +
                "-Milk jugs\n" +
                "-Shampoo bottles\n" +
                "-Detergent bottles\n" +
                "-Vitamin bottles\n" +
                "-Plastic soda bottles\n" +
                "-Water bottles");

        ImageView paperImage = (ImageView) findViewById(R.id.paperImage);
        paperImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.paper));
        TextView paperText = (TextView) findViewById(R.id.paperText);
        paperText.setText("Paper and Paperboard\n" +
                "\n" +
                "-Office paper\n" +
                "-Notebook paper\n" +
                "-Cereal boxes\n" +
                "-Non-Styrofoam egg cartons\n" +
                "-Some pre-packaged food boxes\n" +
                "-Newspaper\n" +
                "-Magazines and slick inserts, e.g. ads that arrive in the mail\n" +
                "* Some paper items like paper milk cartons and drink boxes are lined with wax or plastic and are often not accepted.\n" +
                "-Corrugated Cardboard\n" +
                "\n" +
                "-Shipping and packaging boxes\n" +
                "* Used pizza boxes are often NOT accepted.");
    }
}
