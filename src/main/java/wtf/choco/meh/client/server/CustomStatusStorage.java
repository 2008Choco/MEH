package wtf.choco.meh.client.server;

import com.google.common.collect.Iterators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.network.chat.Component;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.util.Components;

public final class CustomStatusStorage implements Iterable<Component> {

    private final Path path;
    private final List<Component> statuses = new ArrayList<>();

    public CustomStatusStorage(Path path) {
        this.path = path;
    }

    public void addStatus(Component status) {
        this.statuses.add(status);
    }

    public void setStatus(int index, Component status) {
        this.statuses.set(index, status);
    }

    public Component getStatus(int index) {
        return statuses.get(index);
    }

    public void removeStatus(int index) {
        this.statuses.remove(index);
    }

    public int getStatusCount() {
        return statuses.size();
    }

    @Override
    public Iterator<Component> iterator() {
        return Iterators.unmodifiableIterator(statuses.iterator());
    }

    // Probably shouldn't write to file as legacy text, but it's fine
    public void saveToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            StringBuilder data = new StringBuilder();
            this.statuses.forEach(status -> data.append(Components.toLegacyText(status, '&')).append('\n'));
            writer.write(data.toString());
        } catch (IOException e) {
            MEHClient.LOGGER.warn("Couldn't write custom statuses to file \"{}\"", path.toString());
            e.printStackTrace();
        }
    }

    public void readFromFile() {
        if (!Files.exists(path)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            reader.lines().forEach(line -> statuses.add(Components.fromLegacyText(line, '&')));
        } catch (IOException e) {
            MEHClient.LOGGER.warn("Couldn't read custom statuses from file \"{}\"", path.toString());
            e.printStackTrace();
        }
    }

}
