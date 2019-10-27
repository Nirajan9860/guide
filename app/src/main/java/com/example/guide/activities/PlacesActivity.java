package com.example.guide.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guide.Modal.Places;
import com.example.guide.R;
import com.example.guide.adapters.PlacesAdapter;
import com.example.guide.lib.springyRecyclerView.SpringyAdapterAnimator;

import java.util.ArrayList;
import java.util.List;

public class PlacesActivity extends AppCompatActivity {


    Activity activity;
    Context context;
    private RecyclerView recyclerView;
    private List<Places> placesList;
    private SpringyAdapterAnimator springyAdapterAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        context = this;
        activity = PlacesActivity.this;

        recyclerView = findViewById(R.id.places_recyclerView);
        placesList = new ArrayList<>();
        placesList.add(new Places("Taumadhi Square\n" +
                "\tTaumadhi is the next prominent square of the city where the awesome Nyatapolo temple exists which is Nepal’s tallest ancient structure, built by King Bhupatindra Malla. It stands in five tiers and is balanced by the five foundation platforms that stand at the base. From as far back as you can stand, it looks like a fretted pyramid climbing up to the clouds, reaching a height of more than 30 meters. Its inspiration is said to have been a form of Bhairav, who stands in another nearby temple. Certainly no menace terrifies those who swarm over its plinth and up its steps, which are guarded on each side by legendary sentinels. Jaya Mal and Patta, two wrestlers said to have the strength of 10 men, are at the bottom. Next come two huge elephants, each 10 times stronger than the wrestlers, then two lions, each as strong as 10 elephants, two griffins, each as strong as 10 lions and finally on the uppermost plinth, two demi-goddess, Baghini in the form of tigress, and Singhini, as a lioness, each 10 times as strong as griffin. Siddhi Lakshmi, to whom the temple is dedicated, is consequently the most powerful of all these figures. She is depicted with other deities on the struts. Even the caretaker priests can only see the image of the goddess inside the temple and night. It is a pattern of guardian sentinels found nowhere else in Nepali temple architecture and is considered significant evidence of measure of appeasement required to placate Bhairav.", "nyatapolo"));
        placesList.add(new Places("Bhaktapur Durbar Square", "nirajan"));
        placesList.add(new Places("Bhaktapur Durbar Square", "pressure"));


        PlacesAdapter adapter = new PlacesAdapter(placesList, recyclerView, context, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

    }
}
