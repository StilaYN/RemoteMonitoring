package com.example.remotemonitoring.core.service;

import android.content.Context;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class FileService {
    private Context context;

    public FileService(Context context) {
        this.context = context;
    }

    public <T> void write(String fileName, T object) {
        Gson gson = new Gson();
        String data = gson.toJson(object);
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Error writing file", e);
        }
    }

    public <T> T read(String fileName, Type type) {
        Gson gson = new Gson();
        try (FileInputStream fis = context.openFileInput(fileName)) {
            InputStreamReader streamReader = new InputStreamReader(fis);
            return gson.fromJson(streamReader, type);
        } catch (IOException e) {
            return null;
        }
    }
}