package wtf.choco.meh.client.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.include.com.google.common.base.Preconditions;

public final class ChannelSelector {

    private int selectedChannelIndex = 0;
    private final List<ChatChannel> channels = new ArrayList<>();
    private final Map<String, ChatChannel> channelsById = new HashMap<>();

    {
        this.registerDefaultChannel("all", 0xD4D4D4, null);
    }

    private void registerDefaultChannel(String id, int color, String commandPrefix) {
        this.addChannel(new ChatChannel(id, color, commandPrefix, ChatChannelType.GLOBAL, null));
    }

    public int addChannel(ChatChannel channel) {
        String id = channel.getId();
        Preconditions.checkArgument(!channelsById.containsKey(id), "Can't register duplicate channel id: \"%s\"", id);

        int index = channels.size();
        this.channels.add(channel);
        this.channelsById.put(id, channel);
        return index;
    }

    public void removeChannel(String id) {
        ChatChannel channel = channelsById.remove(id);
        this.channels.remove(channel);

        while (selectedChannelIndex >= channels.size()) {
            this.selectedChannelIndex--;
        }
    }

    public void removeChannel(ChatChannel channel) {
        this.removeChannel(channel.getId());
    }

    public boolean exists(String id) {
        return channelsById.containsKey(id);
    }

    public ChatChannel getSelectedChannel() {
        return channels.get(selectedChannelIndex);
    }

    public ChatChannel nextChannel() {
        this.selectedChannelIndex++;
        this.selectedChannelIndex %= channels.size();
        return getSelectedChannel();
    }

    public ChatChannel getNextChannel() {
        return channels.get((selectedChannelIndex + 1) % channels.size());
    }

    public ChatChannel previousChannel() {
        if (--selectedChannelIndex < 0) {
            this.selectedChannelIndex = (channels.size() - 1);
        }

        return getSelectedChannel();
    }

    public ChatChannel getPreviousChannel() {
        int index = selectedChannelIndex - 1;
        if (index < 0) {
            index += channels.size();
        }

        return channels.get(index);
    }

    public ChatChannel selectChannel(int index) {
        Preconditions.checkArgument(index >= 0 && index < channels.size(), "index must be between 0 and %d (inclusive)", channels.size());
        this.selectedChannelIndex = index;
        return getSelectedChannel();
    }

}
