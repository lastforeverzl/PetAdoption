package com.zackyzhang.petadoption.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lei on 8/16/17.
 */

public class FavoriteDataContract {

    public static final String CONTENT_AUTHORITY = "com.zackyzhang.petadoption";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE = "favorite";

    private FavoriteDataContract() {}

    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE)
                .build();

        public static final String TABLE_NAME = "favorite";

        public static final String COLUMN_PET_ID = "pet_id";

        public static final String COLUMN_PET_NAME = "pet_name";

        public static final String COLUMN_PET_INFO = "pet_info";

        public static final String COLUMN_PET_PHOTO = "pet_photo";

        public static final String COLUMN_PET_DATE = "pet_date";

        public static final String COLUMN_PET_STATUS = "pet_status";

        public static final String COLUMN_PET_AGE = "pet_age";

        public static final String COLUMN_PET_SIZE = "pet_size";

        public static final String COLUMN_PET_SHELTER_PET_ID = "pet_shelter_pet_id";

        public static final String COLUMN_PET_BREEDS = "pet_breeds";

        public static final String COLUMN_PET_MEDIA = "pet_media";

        public static final String COLUMN_PET_SEX = "pet_sex";

        public static final String COLUMN_PET_DESCRIPTION = "pet_description";

        public static final String COLUMN_PET_MIX = "pet_mix";

        public static final String COLUMN_PET_SHELTER_ID = "pet_shelter_id";

        public static final String COLUMN_PET_ANIMAL = "pet_animal";

        public static Uri buildPetUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);

        }
    }
}
