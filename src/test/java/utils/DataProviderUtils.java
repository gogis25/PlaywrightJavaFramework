package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class DataProviderUtils {
    public static Object[][] getJsonDataToMap(String filePath) throws IOException {
        String jsonContent = new String(Files.readAllBytes(
                Paths.get(System.getProperty("user.dir")+filePath)
        ));
        Type type = new TypeToken<List<HashMap<String, Object>>>() {}.getType();

        List<HashMap<String, Object>> data =
                new Gson().fromJson(jsonContent, type);

        Object[][] table = new Object[data.size()][1];

        for (int i = 0; i < data.size(); i++) {
            table[i][0] = data.get(i);
        }

        return table;
    }
}
