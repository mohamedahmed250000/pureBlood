package elteam.pureblood.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import elteam.pureblood.R;
import elteam.pureblood.models.Bank;
import elteam.pureblood.models.ItemTips;

public class BanksAdapter extends RecyclerView.Adapter<BanksAdapter.ItemHolder> {

    private Context context;
    private List<Bank> banksList;

    public BanksAdapter(Context context, List<Bank> banksList) {
        this.context = context;
        this.banksList = banksList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_banks, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {

        final Bank currentBank = banksList.get(position);

        itemHolder.name.setText(currentBank.getBank_name());
        itemHolder.address.setText(currentBank.getBank_address());
        itemHolder.phone.setText(currentBank.getBank_phone());

        itemHolder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri = Uri.parse(currentBank.getBank_url());

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                mapIntent.setPackage("com.google.android.apps.maps");

                context.startActivity(mapIntent);
            }
        });

        itemHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + currentBank.getBank_phone()));

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (banksList.isEmpty()) {
            return 0;
        } else {
            return banksList.size();
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private TextView name, address, phone;
        private ImageView location, call;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.bank_name);
            address = itemView.findViewById(R.id.bank_address);
            phone = itemView.findViewById(R.id.bank_phone);
            location = itemView.findViewById(R.id.bank_location);
            call = itemView.findViewById(R.id.bank_call);

        }
    }
}