package ca.limin.flickrphoto.glide_cache;

import com.bumptech.glide.load.Key;

/**
 * A class for keeping track of the cache key of the original data + any requested signature.
 */
class OriginalKey /*implements Key*/ {

    private final String id;
    private final Key signature;

    public OriginalKey(String id, Key signature) {
        this.id = id;
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OriginalKey that = (OriginalKey) o;

        if (!id.equals(that.id)) {
            return false;
        }
        if (!signature.equals(that.signature)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + signature.hashCode();
        return result;
    }
}
