package elteam.pureblood.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import elteam.pureblood.R;
import elteam.pureblood.adapters.BanksAdapter;
import elteam.pureblood.adapters.TipsAdapter;
import elteam.pureblood.models.Bank;
import elteam.pureblood.models.ItemTips;

import static elteam.pureblood.activities.HomeActivity.loadLocale;

public class BloodBanksActivity extends AppCompatActivity {

    private RecyclerView banksRecyclerView;
    private BanksAdapter adapter;
    private List<Bank> banks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        setContentView(R.layout.activity_blood_banks);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.blood_banks));

        banksRecyclerView = findViewById(R.id.recycler_banks);
        banks = new ArrayList<>();

        banks.add(new Bank(getString(R.string.bankname1), getString(R.string.bankaddress1),
                getString(R.string.bankphone1),"geo:30.0503246,31.2097237?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname2), getString(R.string.bankaddress2),
                getString(R.string.bankphone2),"geo:30.0681329,31.2775123?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname3), getString(R.string.bankaddress3),
                getString(R.string.bankphone3),"geo:30.0601234,31.2466433?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname4), getString(R.string.bankaddress4),
                getString(R.string.bankphone4),"geo:30.0859146,31.3020149?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname5), getString(R.string.bankaddress5),
                getString(R.string.bankphone5),"geo:30.012301,31.2305774?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname6), getString(R.string.bankaddress6),
                getString(R.string.bankphone6),"geo:30.0423926,31.212814?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname7), getString(R.string.bankaddress7),
                getString(R.string.bankphone7),"geo:30.4702587,31.1786449?q=blood bank" ));

        banks.add(new Bank(getString(R.string.bankname8), getString(R.string.bankaddress8),
                getString(R.string.bankphone8),"geo:30.5751029,31.010953?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname9), getString(R.string.bankaddress9),
                getString(R.string.bankphone9),"geo:31.1151892,30.9436423?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname10), getString(R.string.bankaddress10),
                getString(R.string.bankphone10),"geo:31.198047,29.9072782?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname11), getString(R.string.bankaddress11),
                getString(R.string.bankphone11),"geo:30.5960766,32.2746441?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname12), getString(R.string.bankaddress12),
                getString(R.string.bankphone12),"geo:27.1794275,31.1839606?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname13), getString(R.string.bankaddress13),
                getString(R.string.bankphone13),"geo:29.0419274,31.1207167?q=blood bank"));

        banks.add(new Bank(getString(R.string.bankname14), getString(R.string.bankaddress14),
                getString(R.string.bankphone14),"geo:24.0579251,32.8844701?q=blood bank"));

        banksRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        adapter = new BanksAdapter(this, banks);
        banksRecyclerView.setAdapter(adapter);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}