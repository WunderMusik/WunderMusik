package ru.bmstu.wundermusik;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ru.bmstu.wundermusik.fragments.PlayerFragment;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            PlayerFragment firstFragment = new PlayerFragment();
            firstFragment.setArguments(getPlayerArguments());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, firstFragment)
                    .commit();
        }
    }

    private Bundle getPlayerArguments() {
        Bundle args = new Bundle();
        // TODO: serialize model to Bundle here
        args.putString("title", "Rockstar");
        args.putString("artist", "Nickelback");
        return args;
    }

    public void onClickControl(View btn) {
        Toast.makeText(this, "control", Toast.LENGTH_SHORT).show();
    }
    public void onClickNext(View btn) {
        Toast.makeText(this, "next", Toast.LENGTH_SHORT).show();
    }
    public void onClickPrevious(View btn) {
        Toast.makeText(this, "previous", Toast.LENGTH_SHORT).show();
    }
}
