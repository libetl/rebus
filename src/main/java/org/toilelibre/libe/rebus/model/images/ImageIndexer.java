package org.toilelibre.libe.rebus.model.images;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * This class transform a "images.txt" input into a relation between words and
 * pictures. It is useful for getting our dictionary and to display the pictures
 * in the JPanel
 *
 * @author LiBe
 *
 */
public class ImageIndexer {

    /**
     * Loads a "images.txt" in the class loader, then transform the input into a
     * Map<String, String>
     *
     * @param cl
     *            class loader
     * @param filename
     *            file name
     * @return Map. The keys are the words and the values are the pictures
     * @throws IOException
     *             if the file could not be read
     */
    public static Map<String, String> index (final String fileName) throws IOException {
        final Map<String, String> indexMap = new HashMap<String, String> ();

        final Properties props = new Properties ();
        // This method is supposed to deal with reading the file
        try {
            props.load (new FileInputStream (new File (fileName)));
        } catch (final FileNotFoundException e) {
        }

        for (final Entry<Object, Object> entry : props.entrySet ()) {
            indexMap.put (entry.getKey ().toString (), entry.getValue ().toString ());
        }

        return indexMap;
    }

}
