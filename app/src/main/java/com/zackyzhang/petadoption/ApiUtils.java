package com.zackyzhang.petadoption;

import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.api.model.ShelterBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * Created by lei on 8/4/17.
 */

public class ApiUtils {
    private static final String TAG = "ApiUtils";

    private static final String ZIPCODE_PATTERN = "\\d{5}";

    public static String cleanInvalidSymbol(String json) {
        String newJson = json
                .replace("$t", "value")
                .replace("@encoding", "encoding")
                .replace("@version", "version")
                .replace("@size", "size")
                .replace("@id", "id")
                .replace("@xmlns:xsi", "xmls_xsi")
                .replace("@xsi:noNamespaceSchemaLocation", "xsi_noNamespaceSchemaLocation");
        return newJson;
    }

    public static String fixBreedObject(String json) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
            JSONObject petFinder = jsonObj.getJSONObject("petfinder");
            if (petFinder.has("pets")) {
                JSONArray pets = petFinder.getJSONObject("pets").getJSONArray("pet");
                for (int i = 0; i < pets.length(); i++) {
                    JSONObject pet = pets.getJSONObject(i);
                    JSONObject breeds = pet.getJSONObject("breeds");
                    Object breed = breeds.get("breed");
                    if (breed instanceof JSONObject) {
                        JSONArray jsonArray = new JSONArray();
                        pet.getJSONObject("breeds").put("breed", jsonArray.put(breed));
                    } else if (breed instanceof JSONArray) {
                        // no-op
                    }
                }
            } else if (petFinder.has("pet")) {
                JSONObject pet = petFinder.getJSONObject("pet");
                JSONObject breeds = pet.getJSONObject("breeds");
                Object breed = breeds.get("breed");
                if (breed instanceof JSONObject) {
                    JSONArray jsonArray = new JSONArray();
                    pet.getJSONObject("breeds").put("breed", jsonArray.put(breed));
                } else if (breed instanceof JSONArray) {
                    // no-op
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.tag("fixBreedObject").e(e.getMessage());
        }
        return jsonObj.toString();
    }

    public static String getFirstPhotoUrl(PetBean pet) {
        if (pet.getMedia() != null && pet.getMedia().getPhotos() != null) {
            List<PetBean.MediaBean.PhotosBean.PhotoBean> photos =
                    pet.getMedia().getPhotos().getPhoto();
            for (PetBean.MediaBean.PhotosBean.PhotoBean photo : photos) {
                if (photo.getId().equals("1") && photo.getSize().equals("x")) {
                    return photo.getValue();
                }
            }
        }
        return null;
    }

    public static List<String> getPhotoUrls(PetBean pet) {
        List<String> urls = new ArrayList<>();
        if (pet.getMedia() != null && pet.getMedia().getPhotos() != null) {
            List<PetBean.MediaBean.PhotosBean.PhotoBean> photos =
                    pet.getMedia().getPhotos().getPhoto();
            for (PetBean.MediaBean.PhotosBean.PhotoBean photo : photos) {
                if (photo.getSize().equals("x")) {
                    urls.add(photo.getValue());
                }
            }
        }
        return urls;
    }

    public static String getPhotoUrlsString(PetBean pet) {
        List<String> urls = getPhotoUrls(pet);
        String urlStr = "";
        for (String url : urls) {
            urlStr += url + ";";
        }
        return urlStr;
    }

    public static String getBreedsWithSemiColon(PetBean pet) {
        String breed = "";
        if (pet.getBreeds() != null) {
            for (PetBean.BreedsBean.BreedBean item : pet.getBreeds()) {
                breed += item.getValue() + ";";
            }
        }
        return breed;
    }

    public static String getPetLocation(PetBean.ContactBean contact) {
        String state = contact.getState();
        String city = contact.getCity();
        return String.format("%s, %s", city, state);
    }

    public static String getPetInfo(PetBean pet) {
        String formatString = "%s - %s - %s - %s";
        String size = "";
        if (Constants.petSizeMap.containsKey(pet.getSize()))
            size = Constants.petSizeMap.get(pet.getSize());
        String age = pet.getAge();
        String sex;
        if (pet.getSex().equals("M"))
            sex = "male";
        else
            sex = "female";
        String breed = getBreedString(pet);
        return String.format(formatString, size, age, sex, breed);
    }

    public static String getBreedString(PetBean pet) {
        String breed = "";
        for (PetBean.BreedsBean.BreedBean item : pet.getBreeds()) {
            breed += item.getValue() + " & ";
        }
        return breed.substring(0, breed.length() - 3);
    }

    public static String getAdoptionStatus(PetBean pet) {
        if (pet.getLastUpdate() == null) {
            return null;
        }
        String status = "";
        if (Constants.petStatusMap.containsKey(pet.getStatus())) {
            status = Constants.petStatusMap.get(pet.getStatus());
        }
        return status;
    }

    public static String getLastUpdate(PetBean pet) {
        if (pet.getLastUpdate() == null) {
            return null;
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        DateFormat newDf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = df.parse(pet.getLastUpdate());
        } catch (ParseException e) {
            Timber.tag(TAG).d(e.getMessage());
            e.printStackTrace();
        }
        String newDateString = newDf.format(date);
        Timber.tag(TAG).d(newDateString);
        return newDateString;
    }

    public static String shelterAddress(ShelterBean shelter) {
        String address1 = "";
        String address2 = "";
        String city = "";
        String state = "";
        String country = "";
        String zipCode = "";
        if (shelter.getAddress1() != null)
            address1 = shelter.getAddress1();
        if (shelter.getAddress2() != null)
            address2 = shelter.getAddress2();
        if (shelter.getCity() != null)
            city = shelter.getCity();
        if (shelter.getState() != null)
            state = shelter.getState();
        if (shelter.getZip() != null)
            zipCode = shelter.getZip();
        if (shelter.getCountry() != null)
            country = shelter.getCountry();
        Timber.tag(TAG).d(shelter.getName() + ": " + address1 + " " + address2 + " " + city + " " + state + " " + zipCode + " " + country);
        return (address1 + " " + address2 + ", " + city + ", " + state + " " + zipCode + " " + country).trim();

    }

    public static String getZipCodeFromAddress(String address) {
        String pattern = ZIPCODE_PATTERN;
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(address);
        if (m.find()) {
            return m.group();
        }
        return null;
    }

    public static String cleanPhoneNumber(String phoneNumber) {
        return phoneNumber.trim().replaceAll("\\D", "");
    }

    public static Map<String, String> getPetFindOptions(String type, String size, String sex, String age) {
        Map<String, String> options = new HashMap<>();
        if (!type.equals("any")) {
            options.put("animal", type);
        }
        if (!size.equals("any")) {
            options.put("size", size);
        }
        if (!sex.equals("any")) {
            options.put("sex", sex);
        }
        if (!age.equals("any")) {
            options.put("age", age);
        }
        return options;
    }

    public static List<PetBean.BreedsBean.BreedBean> getBreedsList(String string) {
        String[] breeds = string.split(";");
        List<PetBean.BreedsBean.BreedBean> list = new ArrayList<>();
        for (String item : breeds) {
            PetBean.BreedsBean.BreedBean breed = new PetBean.BreedsBean.BreedBean(item);
            list.add(breed);
        }
        return list;
    }

    public static List<PetBean.MediaBean.PhotosBean.PhotoBean> getPhotoList(String string) {
        String[] urls = string.split(";");
        List<PetBean.MediaBean.PhotosBean.PhotoBean> list = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            PetBean.MediaBean.PhotosBean.PhotoBean photo = new PetBean.MediaBean.PhotosBean.PhotoBean();
            photo.setId(String.valueOf(i + 1));
            photo.setSize("x");
            photo.setValue(urls[i]);
            list.add(photo);
        }
        return list;
    }
}
