package de.jpp.model;

import de.jpp.model.interfaces.Graph;
import de.jpp.model.interfaces.ObservableWeightedGraphImpl;

import java.util.Map;

/**
 * A LabelMapGraph. <br>
 * The abstract-tag is only set because the tests will not compile otherwise. You should remove it!
 */
public  class LabelMapGraph extends ObservableWeightedGraphImpl<String ,Map<String ,String>> implements Graph<String, Map<String, String>> {


}
