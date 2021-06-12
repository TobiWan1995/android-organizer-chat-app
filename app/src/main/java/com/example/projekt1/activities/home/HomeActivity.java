package com.example.projekt1.activities.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.projekt1.R;
import com.example.projekt1.activities.login.LoginActivity;
import com.example.projekt1.dialog.AddChatDialog;
import com.example.projekt1.dialog.AddUserDialog;
import com.example.projekt1.models.Chat;
import com.example.projekt1.models.Session;
import com.example.projekt1.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity implements AddChatDialog.ChatDialogListener, AddUserDialog.UserDialogListener
{
    public static Context context;

    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get User-Table-Reference from FireDB
    DatabaseReference userref = root.getReference("User");
    // Get Chat-Table-Reference from FireDB
    DatabaseReference chatref = root.getReference("Chat");
    // Get Chat-Table-Reference from FireDB

    // Deklarieren von Variablen
    RecyclerView recyclerView;
    Home home;
    ImageButton addChatButton;
    ImageButton addUsersButton;

    // Session for current-user
    Session session;

    ArrayList<Chat> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // init session
        session = new Session(LoginActivity.context);

        // init home elements
        addChatButton = findViewById(R.id.addChatsButton);
        addChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChatDialog addChatDialog = new AddChatDialog();
                addChatDialog.show(getSupportFragmentManager(), "Add Chat - Dialog");
            }
        });

        addUsersButton = findViewById(R.id.addUsersButton);
        addUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUserDialog addUserDialog = new AddUserDialog();
                addUserDialog.show(getSupportFragmentManager(), "Add User - Dialog");
            }
        });

        // Chats
        chats = new ArrayList<Chat>();

        // TODO: check current-user - remove later
        System.out.println("Current-User: " + session.getUserName() + ", " + session.getId());

        // dummy-chat firebase
        // Chat chat1 = new Chat("1", "Chat1 - Firebase", new ArrayList<String>());
        // chat1.addUser(session.getId());
        // chat1.addUser("0");
        // chatref.child(String.valueOf(chat1.getId())).setValue(chat1);

        HomeActivity.context = getApplicationContext();

        // Home - RecyclerView - Implementation
        recyclerView = findViewById(R.id.home_activity_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.context));

        home = new Home(chats);
        recyclerView.setAdapter(home);

        // init chat data with data from firebase
        chatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chats.clear();
                for(DataSnapshot child : snapshot.getChildren()){
                    // get current chat - will always hold an empty userList (missing list support)
                    Chat currChat  = child.getValue(Chat.class);
                    // get userlist of current chat - no Firebase-Support for Array or List
                    // need to fetch ref and iterate through it
                    DataSnapshot userListRef = child.child("users");
                    // iterate through users of chat and check for matches
                    for (int i=0;i< userListRef.getChildrenCount() ;i++) {
                        // get current User of chat
                        String currUser = userListRef.child(String.valueOf(i)).getValue(String.class);
                        if(currUser.equals(session.getId())){
                            chats.add(currChat);
                        }
                    }
                }
                home.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(HomeActivity.context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void applyData(String chatTitle, ArrayList<String> users) {
        System.out.println(chatTitle);
        System.out.println(Arrays.toString(users.toArray()));
        // generate unique id
        String key = chatref.push().getKey();
        // save new chat to firebase
        // chatref.child(key).setValue(new Chat(key, chatTitle, users));
        System.out.println(chatTitle + Arrays.toString(users.toArray()));
    }

    @Override
    public void applyData(ArraySet<String> users) {
        System.out.println(Arrays.toString(users.toArray()));
        // update User
        User updatedUser = new User(session.getId(), session.getFullname(), session.getUserName(), session.geteMail(), session.getPassword(), session.getGender(), session.getBirth());
        updatedUser.addUser(users);
        updatedUser.addUser(session.getUsers());
        userref.child(session.getId()).setValue(updatedUser);

        // update Session
        this.session.setUser(updatedUser);
    }
}