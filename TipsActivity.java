package elteam.pureblood.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import elteam.pureblood.R;
import elteam.pureblood.adapters.TipsAdapter;
import elteam.pureblood.models.ItemTips;

import static elteam.pureblood.Constants.KEY_Tip;
import static elteam.pureblood.activities.HomeActivity.loadLocale;

public class TipsActivity extends AppCompatActivity implements TipsAdapter.OnTipClickListener {

    private RecyclerView tipsList;
    private TipsAdapter adapter;
    private List<ItemTips> tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        setContentView(R.layout.activity_tips);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.tips));

        tipsList = findViewById(R.id.tips_list);
        tips = new ArrayList<>();

        tips.add(new ItemTips(getString(R.string.Q1), getString(R.string.Ans1), R.drawable.tips14));
        tips.add(new ItemTips(getString(R.string.Q2), getString(R.string.Ans2), R.drawable.tips15));
        tips.add(new ItemTips(getString(R.string.Q3), getString(R.string.Ans3), R.drawable.tips16));
        tips.add(new ItemTips(getString(R.string.Q4), getString(R.string.Ans4), R.drawable.tips17));
        tips.add(new ItemTips(getString(R.string.Q5), getString(R.string.Ans5), R.drawable.tips18));
        tips.add(new ItemTips(getString(R.string.Q6), getString(R.string.Ans6), R.drawable.tips19));
        tips.add(new ItemTips(getString(R.string.Q7), getString(R.string.Ans7), R.drawable.tips20));
        tips.add(new ItemTips(getString(R.string.Q8), getString(R.string.Ans8), R.drawable.tips14));
        tips.add(new ItemTips(getString(R.string.Q9), getString(R.string.Ans9), R.drawable.tips15));
        tips.add(new ItemTips(getString(R.string.Q10), getString(R.string.Ans10), R.drawable.tips16));
        tips.add(new ItemTips(getString(R.string.Q11), getString(R.string.Ans11), R.drawable.tips17));
        tips.add(new ItemTips(getString(R.string.Q12), getString(R.string.Ans12), R.drawable.tips18));
        tips.add(new ItemTips(getString(R.string.Q13), getString(R.string.Ans13), R.drawable.tips19));
        tips.add(new ItemTips(getString(R.string.Q14), getString(R.string.Ans14), R.drawable.tips20));
        tips.add(new ItemTips(getString(R.string.Q15), getString(R.string.Ans15), R.drawable.tips14));
        tips.add(new ItemTips(getString(R.string.Q16), getString(R.string.Ans16), R.drawable.tips15));
        tips.add(new ItemTips(getString(R.string.Q17), getString(R.string.Ans17), R.drawable.tips16));
        tips.add(new ItemTips(getString(R.string.Q18), getString(R.string.Ans18), R.drawable.tips17));
        tips.add(new ItemTips(getString(R.string.Q19), getString(R.string.Ans19), R.drawable.tips18));
        tips.add(new ItemTips(getString(R.string.Q20), getString(R.string.Ans20), R.drawable.tips19));

        tipsList.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        adapter = new TipsAdapter(this, tips, this);
        tipsList.setAdapter(adapter);
    }

    @Override
    public void onTipClick(int position) {
        Intent intent = new Intent(TipsActivity.this, DetailsTipsActivity.class);
        intent.putExtra(KEY_Tip, tips.get(position));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
