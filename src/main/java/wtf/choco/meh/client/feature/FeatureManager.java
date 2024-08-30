package wtf.choco.meh.client.feature;

import com.google.common.base.Preconditions;
import com.google.common.reflect.MutableTypeToInstanceMap;
import com.google.common.reflect.TypeToInstanceMap;

import java.util.function.Function;

import wtf.choco.meh.client.MEHClient;

/**
 * A manager where features may be added and handled.
 */
public final class FeatureManager {

    private boolean frozen = false;

    private final TypeToInstanceMap<Feature> features = new MutableTypeToInstanceMap<>();
    private final MEHClient mod;

    /**
     * Construct a new {@link FeatureManager}.
     *
     * @param mod the client mod instance
     */
    public FeatureManager(MEHClient mod) {
        this.mod = mod;
    }

    /**
     * Add a new feature to this manager.
     *
     * @param <T> the feature type
     * @param featureClass the class of the feature
     * @param featureConstructor a constructor to create a new instance of the feature. Generally this can be
     * a constructor method reference to the feature, e.g. {@code MyFeature::new}
     *
     * @throws IllegalStateException if this manager has been initialized and frozen
     */
    public <T extends Feature> void addFeature(Class<T> featureClass, Function<MEHClient, T> featureConstructor) {
        Preconditions.checkState(!frozen, "FeatureManager frozen");
        this.features.putInstance(featureClass, featureConstructor.apply(mod));
    }

    /**
     * Get a feature instance for the given feature type.
     *
     * @param <T> the feature type
     * @param featureClass the class of the feature to get
     *
     * @return the feature instance
     *
     * @throws IllegalArgumentException if a feature with the given type has not been added to this manager
     */
    public <T extends Feature> T getFeature(Class<T> featureClass) {
        T feature = features.getInstance(featureClass);
        Preconditions.checkArgument(feature != null, "No feature with type " + featureClass.getName() + " exists.");
        return feature;
    }

    /**
     * Initialize all the features and freeze this manager from any further mutations.
     *
     * @throws IllegalStateException if this manager has already been initialized and frozen
     */
    public void initializeFeatures() {
        Preconditions.checkState(!frozen, "FeatureManager frozen");

        this.features.values().forEach(Feature::registerListeners);
        this.frozen = true;
    }

}
