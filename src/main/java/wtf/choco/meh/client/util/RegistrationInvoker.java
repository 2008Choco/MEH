package wtf.choco.meh.client.util;

@FunctionalInterface
public interface RegistrationInvoker<K, V> {

    public void register(K key, V value);

}
