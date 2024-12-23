package wtf.choco.meh.client.party;

public enum PartyRole {

    LEADER("Leader"),
    MOD("Moderator"),
    MEMBER("Member");

    private final String prettyName;

    private PartyRole(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }

}
