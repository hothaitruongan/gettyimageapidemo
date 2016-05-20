package truongan.android.shuttershockapidemo.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import truongan.android.shuttershockapidemo.model.BasePhoto;
import truongan.android.shuttershockapidemo.model.PhotoWrapper;

/**
 * Created by truongan91 on 5/17/16.
 */
public class PhotoWrapperDeserializer implements JsonDeserializer<PhotoWrapper> {
    @Override
    public PhotoWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<BasePhoto> photos = new ArrayList<>();
        JsonObject rootObject = json.getAsJsonObject();
        final int totalResult = rootObject.get("result_count").getAsInt();
        final JsonArray itemsArray = rootObject.get("images").getAsJsonArray();
        final int size = itemsArray.size();
        for (int i = 0; i < size; i++) {
            JsonObject jsonObject = itemsArray.get(i).getAsJsonObject();
            final BasePhoto photo = context.deserialize(jsonObject, BasePhoto.class);
            photos.add(photo);
        }
        return new PhotoWrapper(photos);
    }
}
