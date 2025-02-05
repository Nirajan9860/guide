package com.example.guide.ui.placeDetail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.guide.R;
import com.github.florent37.shapeofview.shapes.ArcView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceDetailFragment extends Fragment implements TextToSpeech.OnInitListener, OnMapReadyCallback {

    private PlaceDetailViewModel mViewModel;
    TextToSpeech t1;
    MotionLayout motionLayout;
    View view;

    TextView textView;
    TextView titleText;
    Boolean contentTextFullscreen = false;
    FloatingActionButton b1;
    ImageView imageView;
    View divider;
    CardView cardView, cardViewMap;
    ArcView arc;
    Drawable drawable;
    String name, description, imageName;
    FloatingActionButton navigation;
    ScrollView scrollView;

    int shortAnimationDuration;
    private boolean isPlaying = false;

    public static PlaceDetailFragment newInstance() {
        return new PlaceDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.place_detail_fragment, container, false);

        motionLayout = v.findViewById(R.id.motionLayout);

        imageView = v.findViewById(R.id.coverImage);

        divider = v.findViewById(R.id.divider);
        view = v.findViewById(R.id.bgView);
        textView = v.findViewById(R.id.contentText);
        titleText = v.findViewById(R.id.titleTextView);
        arc = v.findViewById(R.id.myShape);
        cardView = v.findViewById(R.id.tq);
        b1 = v.findViewById(R.id.botton);
        cardViewMap = v.findViewById(R.id.cardViewMap);
        navigation = v.findViewById(R.id.navigation);
        scrollView = v.findViewById(R.id.contentTextView);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (scrollView.getChildAt(0).getBottom()
                                <= (scrollView.getHeight() + scrollView.getScrollY())) {
                            motionLayout.transitionToEnd();
                        } else if(scrollView.getScrollY() == 0 ) {
                            //scroll view is not at bottom
                            motionLayout.transitionToStart();
                        }
                    }
                });
        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
                contentTextFullscreen = v == 1;
                String name = "rectangle";
                int trans = (int) (v * 10);
                switch (trans) {
                    case 0:
                        name += trans;
                        //set the animation
                        break;
                    case 1:
                        name += trans;
                        break;
                    case 2:
                        name += trans;
                        break;
                    case 3:
                        name += trans;
                        break;
                    case 4:
                        name += trans;
                        break;
                    case 5:
                        name += trans;
                        break;
                    case 6:
                        name += trans;
                        break;
                    case 7:
                        name += trans;
                        break;
                    case 8:
                        name += trans;
                        break;
                    case 9:
                        name += trans;
                        break;
                    case 10:
                        name += trans;


                        break;
                    default:
                        name += trans;
                        break;

                }

                //   Log.i("Scroll animation", name);
                //   Log.i("Scroll animation", name);
                view.setBackground(getResources().getDrawable(getResources().getIdentifier(name, "drawable", getActivity().getPackageName())));
            }
            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) { }
            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {          }
        });

        description = getArguments().getString("description");
        imageName = getArguments().getString("image");
        name = getArguments().getString("name");

        Pattern p = Pattern.compile("26(.*?)62");
        Matcher m = p.matcher(description);


        while(m.find()){
            textView.append(m.group(1)+"\n\n");
        }
        if(textView.getText().equals("")){
            textView.setText(description);
        }



        titleText.setText(name);

        if (!imageName.equals("")) {
            Glide.with(this)
                    .load(getResources()
                            .getIdentifier(imageName, "drawable", getActivity().getPackageName()))
                    .fitCenter()
                    .override(1000, 1000)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            drawable = resource;
                            imageView.setImageDrawable(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

        }





         t1 = new TextToSpeech(getContext(), this, "com.google.android.tts");
        //        Set<String> a=new HashSet<>();
        //        a.add("female");//here you can give male if you want to select male voice.
        //        Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","UK"),400,200,true,a);
        //        t1.setVoice(v);
        //        t1.setSpeechRate(0.8f);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    if (t1 != null) {
                        t1.stop();
                        b1.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_black_24dp));
                    }
                    isPlaying = false;
                }else{
                    t1.speak(description, TextToSpeech.QUEUE_FLUSH, null);

                    isPlaying = true;
                    b1.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_off_black_24dp));
                }
            }
        });

        return v;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("image", imageName);

                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_placeDetailFragment3_to_fullscreenImageFragment, bundle);
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
//                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
//                PhotoView photoView = mView.findViewById(R.id.imageView);
//                photoView.setImageDrawable(drawable);
//                mBuilder.setView(mView);
//                AlertDialog alertDialog = mBuilder.create();
//                alertDialog.show();
            }
        });


        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("redirect", "marker");
                bundle.putDouble("latitude", getArguments().getDouble("lat"));
                bundle.putDouble("longitude", getArguments().getDouble("long"));
                bundle.putString("title", name);
                bundle.putString("description", description);

                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_placeDetailFragment3_to_nav_map, bundle);
            }
        });


        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (t1 != null) {
                    t1.stop();
                    b1.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_black_24dp));
                }
                if (contentTextFullscreen) {
                    motionLayout.transitionToStart();
                } else {

                    t1.shutdown();


                    Navigation.findNavController(getActivity(),R.id.my_nav_host_fragment).navigateUp();

                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        mViewModel = ViewModelProviders.of(this).get(PlaceDetailViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Set<String> a=new HashSet<>();
            a.add("male");//here you can give male if you want to select male voice.
//            Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","US"),400,200,true,a);
            Voice v=new Voice("en-us-x-sfg#male_2-local",new Locale("en","US"),400,200,true,a);
            t1.setVoice(v);
            t1.setSpeechRate(0.8f);

            //        Set<String> a=new HashSet<>();
            //        a.add("female");//here you can give male if you want to select male voice.
            //        Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","UK"),400,200,true,a);
            //        t1.setVoice(v);
            //        t1.setSpeechRate(0.8f);

            // int result = t1.setLanguage(Locale.US);
            int result = t1.setVoice(v);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");




            } else {
                // btnSpeak.setEnabled(true);

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

        t1.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onDone(String utteranceId) {
                // Log.d("MainActivity", "TTS finished");
                if (t1 != null) {
                    t1.stop();
                    b1.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_black_24dp));
                }
                isPlaying = false;

            }

            @Override
            public void onError(String utteranceId) {
            }

            @Override
            public void onStart(String utteranceId) {

            }
        });
    }

    private void speakOut(String message) {

        t1.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onPause() {
        if (t1 != null) {
            t1.stop();
            b1.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_black_24dp));
        }
        super.onPause();

    }

    @Override
    public void onDestroy() {

        if(t1!= null) {
            t1.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(getArguments().getDouble("lat"), getArguments().getDouble("long"));
        googleMap.addMarker(new MarkerOptions().position(sydney).title(name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

    }
}
