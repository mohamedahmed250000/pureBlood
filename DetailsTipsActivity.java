package elteam.pureblood.activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import elteam.pureblood.R;
import elteam.pureblood.models.ItemTips;

import static elteam.pureblood.Constants.KEY_Tip;
import static elteam.pureblood.activities.HomeActivity.loadLocale;

public class DetailsTipsActivity extends AppCompatActivity {

    private ItemTips itemTips = null;

    private ImageView image_tips;
    private TextView question, answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        setContentView(R.layout.activity_details_tips);
        setToolbar();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(KEY_Tip)){
            itemTips = (ItemTips) intent.getSerializableExtra(KEY_Tip);
        }
        image_tips = findViewById(R.id.image_tips);
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);

        if (itemTips != null){

            image_tips.setImageResource(itemTips.getImage());
            question.setText(itemTips.getQuestion());
            answer.setText(itemTips.getAnswer());
        }
    }

    private void setToolbar() {
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);

        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary));
        collapsingToolbar.setTitle(getString(R.string.details));
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null) {
            this.setSupportActionBar(toolbar);
            ActionBar actionBar = this.getSupportActionBar();
            actionBar.setTitle(getResources().getString(R.string.details));

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
