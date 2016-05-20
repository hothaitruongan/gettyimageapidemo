package truongan.android.shuttershockapidemo.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import truongan.android.shuttershockapidemo.model.PhotoDetail;

/**
 * Created by truongan91 on 5/17/16.
 */
public class PhotoDetailDeserializer implements JsonDeserializer<PhotoDetail> {
    @Override
    public PhotoDetail deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray root = json.getAsJsonObject().getAsJsonArray("images");
        JsonObject child = root.get(0).getAsJsonObject();

        final String id = child.get("id").getAsString();
        final String artist = child.get("artist").getAsString();
        final String caption = child.get("caption").getAsString();
        final JsonArray peopleArray = child.get("people").getAsJsonArray();
        final int pplSize = peopleArray.size();
        final String[] ppls = new String[pplSize];
        for (int i = 0; i < pplSize; i++) {
            ppls[i] = peopleArray.get(i).getAsString();
        }
        final String title = child.get("title").getAsString();
        return new PhotoDetail(id, artist, title, caption, ppls);
    }
}
