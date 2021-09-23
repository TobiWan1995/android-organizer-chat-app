package com.example.projekt1.models.plugins;
import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt1.R;
import com.example.projekt1.activities.chat.ChatActivity;
import com.example.projekt1.models.plugins.pluginData.Poll;
import com.google.firebase.database.collection.LLRBNode;
  import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.pluginData.Poll;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class PluginPoll extends Plugin<Poll> {

    /*  Abstimmung = Poll
        Abstimmungsoptionen = PollOptionen
    TODO:
    Wie eine Abstimmung worked einbauen
        Option für mehrfaches auswählen von PollOptionen
        Anzeigen der Ergebnisse in Prozent
        Poll zurücksetzen

    Firebase anbindung
    Layout und Design beenden
    PollAdapter Klasse speziell onBindViewHolder checken ob worked
    anbindung an bestehende App
    Poll zurücksetzen funktion^^^^
    PollOptionen bennenen und speichern um auf anderen Geräten anzuzeigen
    Ob PollOption ausgewählt wurde muss auch gespeichert werden

    Least Priority:
    Visuelle Darstellung der Ergebnisse
     */

    public PluginPoll (String id, String beschreibung, String chatRef, String typ) {
        super(id, beschreibung, chatRef, "pluginPoll");
    }

    public PluginPoll(String id, String beschreibung, String typ, String chatRef, ArrayList<Poll> polls){
        super(id, beschreibung, typ, chatRef, polls);
    }

    public PluginPoll () {
        super();
    }

    @Override
    public void doPluginStuff() {

    }

    public void updatePollText (String id, String text ){
        this.pluginData = this.pluginData.stream().map(val -> {
            if(id.equals(val.getId())) val.setTitle(text);
            return val;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public void updatePollCheckBox (String id, Boolean isChecked ){ }

    public void addPoll(Poll p){ this.pluginData.add(p);}

    public void setPolls(ArrayList<Poll> newPolls){
        this.pluginData = newPolls;
    }
}
