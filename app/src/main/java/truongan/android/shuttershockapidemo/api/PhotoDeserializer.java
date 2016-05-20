package truongan.android.shuttershockapidemo.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import truongan.android.shuttershockapidemo.model.BasePhoto;

/**
 * Created by truongan91 on 5/17/16.
 */
public class PhotoDeserializer implements JsonDeserializer<BasePhoto> {
    @Override
    public BasePhoto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject rootObject = json.getAsJsonObject();
        final String id = rootObject.get("id").getAsString();
        final String title = rootObject.get("title").getAsString();

        JsonArray displaySizes = rootObject.get("display_sizes").getAsJsonArray();
        JsonObject sizeObject = displaySizes.get(0).getAsJsonObject();
        final String url = sizeObject.get("uri").getAsString();
        return new BasePhoto(id, title, url);
    }
}
