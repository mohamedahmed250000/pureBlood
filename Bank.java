package elteam.pureblood.models;

public class Bank {

    private String bank_name, bank_address, bank_phone, bank_url;

    public Bank(String bank_name, String bank_address, String bank_phone, String bank_url) {
        this.bank_name = bank_name;
        this.bank_address = bank_address;
        this.bank_phone = bank_phone;
        this.bank_url = bank_url;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_address() {
        return bank_address;
    }

    public void setBank_address(String bank_address) {
        this.bank_address = bank_address;
    }

    public String getBank_phone() {
        return bank_phone;
    }

    public void setBank_phone(String bank_phone) {
        this.bank_phone = bank_phone;
    }

    public String getBank_url() {
        return bank_url;
    }

    public void setBank_url(String bank_url) {
        this.bank_url = bank_url;
    }
}
