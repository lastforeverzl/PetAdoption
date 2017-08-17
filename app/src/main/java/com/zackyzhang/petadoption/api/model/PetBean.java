package com.zackyzhang.petadoption.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 8/3/17.
 */

public class PetBean implements Parcelable {
    @SerializedName("status")
    private StatusBean status;
    @SerializedName("contact")
    private ContactBean contact;
    @SerializedName("age")
    private AgeBean age;
    @SerializedName("size")
    private SizeBean size;
    @SerializedName("media")
    private MediaBean media;
    @SerializedName("id")
    private IdBean id;
    @SerializedName("shelterPetId")
    private ShelterPetIdBean shelterPetId;
    @SerializedName("breeds")
    private BreedsBean breeds;
    @SerializedName("name")
    private NameBean name;
    @SerializedName("sex")
    private SexBean sex;
    @SerializedName("description")
    private DescriptionBean description;
    @SerializedName("mix")
    private MixBean mix;
    @SerializedName("shelterId")
    private ShelterIdBean shelterId;
    @SerializedName("lastUpdate")
    private LastUpdateBean lastUpdate;
    @SerializedName("animal")
    private AnimalBean animal;

    public String getStatus() {
        return status.getValue();
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public ContactBean getContact() {
        return contact;
    }

    public void setContact(ContactBean contact) {
        this.contact = contact;
    }

    public String getAge() {
        return age.getValue();
    }

    public void setAge(AgeBean age) {
        this.age = age;
    }

    public String getSize() {
        return size.getValue();
    }

    public void setSize(SizeBean size) {
        this.size = size;
    }

    public MediaBean getMedia() {
        return media;
    }

    public void setMedia(MediaBean media) {
        this.media = media;
    }

    public String getId() {
        return id.getValue();
    }

    public void setId(IdBean id) {
        this.id = id;
    }

    public String getShelterPetId() {
        return shelterPetId.getValue();
    }

    public void setShelterPetId(ShelterPetIdBean shelterPetId) {
        this.shelterPetId = shelterPetId;
    }

    public List<BreedsBean.BreedBean> getBreeds() {
        return breeds.getBreed();
    }

    public void setBreeds(BreedsBean breeds) {
        this.breeds = breeds;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(NameBean name) {
        this.name = name;
    }

    public String getSex() {
        return sex.getValue();
    }

    public void setSex(SexBean sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description.getValue();
    }

    public void setDescription(DescriptionBean description) {
        this.description = description;
    }

    public String getMix() {
        return mix.getValue();
    }

    public void setMix(MixBean mix) {
        this.mix = mix;
    }

    public String getShelterId() {
        return shelterId.getValue();
    }

    public void setShelterId(ShelterIdBean shelterId) {
        this.shelterId = shelterId;
    }

    public String getLastUpdate() {
        return lastUpdate.getValue();
    }

    public void setLastUpdate(LastUpdateBean lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getAnimal() {
        return animal.getValue();
    }

    public void setAnimal(AnimalBean animal) {
        this.animal = animal;
    }

    public static class OptionsBean implements Parcelable {
        @SerializedName("option")
        private List<OptionBean> option;

        public List<OptionBean> getOption() {
            return option;
        }

        public void setOption(List<OptionBean> option) {
            this.option = option;
        }

        public static class OptionBean implements Parcelable {
            @SerializedName("value")
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.value);
            }

            public OptionBean() {
            }

            protected OptionBean(Parcel in) {
                this.value = in.readString();
            }

            public static final Creator<OptionBean> CREATOR = new Creator<OptionBean>() {
                @Override
                public OptionBean createFromParcel(Parcel source) {
                    return new OptionBean(source);
                }

                @Override
                public OptionBean[] newArray(int size) {
                    return new OptionBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.option);
        }

        public OptionsBean() {
        }

        protected OptionsBean(Parcel in) {
            this.option = new ArrayList<OptionBean>();
            in.readList(this.option, OptionBean.class.getClassLoader());
        }

        public static final Creator<OptionsBean> CREATOR = new Creator<OptionsBean>() {
            @Override
            public OptionsBean createFromParcel(Parcel source) {
                return new OptionsBean(source);
            }

            @Override
            public OptionsBean[] newArray(int size) {
                return new OptionsBean[size];
            }
        };
    }

    public static class StatusBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public StatusBean(String value) {
            this.value = value;
        }

        protected StatusBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<StatusBean> CREATOR = new Creator<StatusBean>() {
            @Override
            public StatusBean createFromParcel(Parcel source) {
                return new StatusBean(source);
            }

            @Override
            public StatusBean[] newArray(int size) {
                return new StatusBean[size];
            }
        };
    }

    public static class ContactBean implements Parcelable {
        @SerializedName("phone")
        private PhoneBean phone;
        @SerializedName("state")
        private StateBean state;
        @SerializedName("address2")
        private Address2Bean address2;
        @SerializedName("email")
        private EmailBean email;
        @SerializedName("city")
        private CityBean city;
        @SerializedName("zip")
        private ZipBean zip;
        @SerializedName("fax")
        private FaxBean fax;
        @SerializedName("address1")
        private Address1Bean address1;

        public String getPhone() {
            return phone.getValue();
        }

        public void setPhone(PhoneBean phone) {
            this.phone = phone;
        }

        public String getState() {
            return state.getValue();
        }

        public void setState(StateBean state) {
            this.state = state;
        }

        public Address2Bean getAddress2() {
            return address2;
        }

        public void setAddress2(Address2Bean address2) {
            this.address2 = address2;
        }

        public EmailBean getEmail() {
            return email;
        }

        public void setEmail(EmailBean email) {
            this.email = email;
        }

        public String getCity() {
            return city.getValue();
        }

        public void setCity(CityBean city) {
            this.city = city;
        }

        public ZipBean getZip() {
            return zip;
        }

        public void setZip(ZipBean zip) {
            this.zip = zip;
        }

        public FaxBean getFax() {
            return fax;
        }

        public void setFax(FaxBean fax) {
            this.fax = fax;
        }

        public Address1Bean getAddress1() {
            return address1;
        }

        public void setAddress1(Address1Bean address1) {
            this.address1 = address1;
        }

        public static class PhoneBean implements Parcelable {
            @SerializedName("value")
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.value);
            }

            public PhoneBean() {
            }

            protected PhoneBean(Parcel in) {
                this.value = in.readString();
            }

            public static final Creator<PhoneBean> CREATOR = new Creator<PhoneBean>() {
                @Override
                public PhoneBean createFromParcel(Parcel source) {
                    return new PhoneBean(source);
                }

                @Override
                public PhoneBean[] newArray(int size) {
                    return new PhoneBean[size];
                }
            };
        }

        public static class StateBean implements Parcelable {
            @SerializedName("value")
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.value);
            }

            public StateBean() {
            }

            protected StateBean(Parcel in) {
                this.value = in.readString();
            }

            public static final Creator<StateBean> CREATOR = new Creator<StateBean>() {
                @Override
                public StateBean createFromParcel(Parcel source) {
                    return new StateBean(source);
                }

                @Override
                public StateBean[] newArray(int size) {
                    return new StateBean[size];
                }
            };
        }

        public static class Address2Bean implements Parcelable {
            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
            }

            public Address2Bean() {
            }

            protected Address2Bean(Parcel in) {
            }

            public static final Creator<Address2Bean> CREATOR = new Creator<Address2Bean>() {
                @Override
                public Address2Bean createFromParcel(Parcel source) {
                    return new Address2Bean(source);
                }

                @Override
                public Address2Bean[] newArray(int size) {
                    return new Address2Bean[size];
                }
            };
        }

        public static class EmailBean implements Parcelable {
            @SerializedName("value")
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.value);
            }

            public EmailBean() {
            }

            protected EmailBean(Parcel in) {
                this.value = in.readString();
            }

            public static final Creator<EmailBean> CREATOR = new Creator<EmailBean>() {
                @Override
                public EmailBean createFromParcel(Parcel source) {
                    return new EmailBean(source);
                }

                @Override
                public EmailBean[] newArray(int size) {
                    return new EmailBean[size];
                }
            };
        }

        public static class CityBean implements Parcelable {
            @SerializedName("value")
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.value);
            }

            public CityBean() {
            }

            protected CityBean(Parcel in) {
                this.value = in.readString();
            }

            public static final Creator<CityBean> CREATOR = new Creator<CityBean>() {
                @Override
                public CityBean createFromParcel(Parcel source) {
                    return new CityBean(source);
                }

                @Override
                public CityBean[] newArray(int size) {
                    return new CityBean[size];
                }
            };
        }

        public static class ZipBean implements Parcelable {
            @SerializedName("value")
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.value);
            }

            public ZipBean() {
            }

            protected ZipBean(Parcel in) {
                this.value = in.readString();
            }

            public static final Creator<ZipBean> CREATOR = new Creator<ZipBean>() {
                @Override
                public ZipBean createFromParcel(Parcel source) {
                    return new ZipBean(source);
                }

                @Override
                public ZipBean[] newArray(int size) {
                    return new ZipBean[size];
                }
            };
        }

        public static class FaxBean implements Parcelable {
            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
            }

            public FaxBean() {
            }

            protected FaxBean(Parcel in) {
            }

            public static final Creator<FaxBean> CREATOR = new Creator<FaxBean>() {
                @Override
                public FaxBean createFromParcel(Parcel source) {
                    return new FaxBean(source);
                }

                @Override
                public FaxBean[] newArray(int size) {
                    return new FaxBean[size];
                }
            };
        }

        public static class Address1Bean implements Parcelable {
            @SerializedName("value")
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.value);
            }

            public Address1Bean() {
            }

            protected Address1Bean(Parcel in) {
                this.value = in.readString();
            }

            public static final Creator<Address1Bean> CREATOR = new Creator<Address1Bean>() {
                @Override
                public Address1Bean createFromParcel(Parcel source) {
                    return new Address1Bean(source);
                }

                @Override
                public Address1Bean[] newArray(int size) {
                    return new Address1Bean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.phone, flags);
            dest.writeParcelable(this.state, flags);
            dest.writeParcelable(this.address2, flags);
            dest.writeParcelable(this.email, flags);
            dest.writeParcelable(this.city, flags);
            dest.writeParcelable(this.zip, flags);
            dest.writeParcelable(this.fax, flags);
            dest.writeParcelable(this.address1, flags);
        }

        public ContactBean() {
        }

        protected ContactBean(Parcel in) {
            this.phone = in.readParcelable(PhoneBean.class.getClassLoader());
            this.state = in.readParcelable(StateBean.class.getClassLoader());
            this.address2 = in.readParcelable(Address2Bean.class.getClassLoader());
            this.email = in.readParcelable(EmailBean.class.getClassLoader());
            this.city = in.readParcelable(CityBean.class.getClassLoader());
            this.zip = in.readParcelable(ZipBean.class.getClassLoader());
            this.fax = in.readParcelable(FaxBean.class.getClassLoader());
            this.address1 = in.readParcelable(Address1Bean.class.getClassLoader());
        }

        public static final Creator<ContactBean> CREATOR = new Creator<ContactBean>() {
            @Override
            public ContactBean createFromParcel(Parcel source) {
                return new ContactBean(source);
            }

            @Override
            public ContactBean[] newArray(int size) {
                return new ContactBean[size];
            }
        };
    }

    public static class AgeBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public AgeBean(String value) {
            this.value = value;
        }

        protected AgeBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<AgeBean> CREATOR = new Creator<AgeBean>() {
            @Override
            public AgeBean createFromParcel(Parcel source) {
                return new AgeBean(source);
            }

            @Override
            public AgeBean[] newArray(int size) {
                return new AgeBean[size];
            }
        };
    }

    public static class SizeBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public SizeBean(String value) {
            this.value = value;
        }

        protected SizeBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<SizeBean> CREATOR = new Creator<SizeBean>() {
            @Override
            public SizeBean createFromParcel(Parcel source) {
                return new SizeBean(source);
            }

            @Override
            public SizeBean[] newArray(int size) {
                return new SizeBean[size];
            }
        };
    }

    public static class MediaBean implements Parcelable {
        @SerializedName("photos")
        private PhotosBean photos;

        public PhotosBean getPhotos() {
            return photos;
        }

        public void setPhotos(PhotosBean photos) {
            this.photos = photos;
        }

        public static class PhotosBean implements Parcelable {
            @SerializedName("photo")
            private List<PhotoBean> photo;

            public List<PhotoBean> getPhoto() {
                return photo;
            }

            public void setPhoto(List<PhotoBean> photo) {
                this.photo = photo;
            }

            public static class PhotoBean implements Parcelable {
                @SerializedName("size")
                private String size;
                @SerializedName("value")
                private String value;
                @SerializedName("id")
                private String id;

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.size);
                    dest.writeString(this.value);
                    dest.writeString(this.id);
                }

                public PhotoBean() {
                }

                protected PhotoBean(Parcel in) {
                    this.size = in.readString();
                    this.value = in.readString();
                    this.id = in.readString();
                }

                public static final Creator<PhotoBean> CREATOR = new Creator<PhotoBean>() {
                    @Override
                    public PhotoBean createFromParcel(Parcel source) {
                        return new PhotoBean(source);
                    }

                    @Override
                    public PhotoBean[] newArray(int size) {
                        return new PhotoBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeList(this.photo);
            }

            public PhotosBean() {
            }

            protected PhotosBean(Parcel in) {
                this.photo = new ArrayList<PhotoBean>();
                in.readList(this.photo, PhotoBean.class.getClassLoader());
            }

            public static final Creator<PhotosBean> CREATOR = new Creator<PhotosBean>() {
                @Override
                public PhotosBean createFromParcel(Parcel source) {
                    return new PhotosBean(source);
                }

                @Override
                public PhotosBean[] newArray(int size) {
                    return new PhotosBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.photos, flags);
        }

        public MediaBean() {
        }

        protected MediaBean(Parcel in) {
            this.photos = in.readParcelable(PhotosBean.class.getClassLoader());
        }

        public static final Creator<MediaBean> CREATOR = new Creator<MediaBean>() {
            @Override
            public MediaBean createFromParcel(Parcel source) {
                return new MediaBean(source);
            }

            @Override
            public MediaBean[] newArray(int size) {
                return new MediaBean[size];
            }
        };
    }

    public static class IdBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public IdBean(String value) {
            this.value = value;
        }

        protected IdBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<IdBean> CREATOR = new Creator<IdBean>() {
            @Override
            public IdBean createFromParcel(Parcel source) {
                return new IdBean(source);
            }

            @Override
            public IdBean[] newArray(int size) {
                return new IdBean[size];
            }
        };
    }

    public static class ShelterPetIdBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public ShelterPetIdBean(String value) {
            this.value = value;
        }

        protected ShelterPetIdBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<ShelterPetIdBean> CREATOR = new Creator<ShelterPetIdBean>() {
            @Override
            public ShelterPetIdBean createFromParcel(Parcel source) {
                return new ShelterPetIdBean(source);
            }

            @Override
            public ShelterPetIdBean[] newArray(int size) {
                return new ShelterPetIdBean[size];
            }
        };
    }

    public static class BreedsBean implements Parcelable {
        @SerializedName("breed")
        private List<BreedBean> breed;

        public List<BreedBean> getBreed() {
            return breed;
        }

        public void setBreed(List<BreedBean> breed) {
            this.breed = breed;
        }

        public static class BreedBean implements Parcelable {
            @SerializedName("value")
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.value);
            }

            public BreedBean(String value) {
                this.value = value;
            }

            protected BreedBean(Parcel in) {
                this.value = in.readString();
            }

            public static final Creator<BreedBean> CREATOR = new Creator<BreedBean>() {
                @Override
                public BreedBean createFromParcel(Parcel source) {
                    return new BreedBean(source);
                }

                @Override
                public BreedBean[] newArray(int size) {
                    return new BreedBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.breed);
        }

        public BreedsBean() {
        }

        protected BreedsBean(Parcel in) {
            this.breed = new ArrayList<BreedBean>();
            in.readList(this.breed, BreedBean.class.getClassLoader());
        }

        public static final Creator<BreedsBean> CREATOR = new Creator<BreedsBean>() {
            @Override
            public BreedsBean createFromParcel(Parcel source) {
                return new BreedsBean(source);
            }

            @Override
            public BreedsBean[] newArray(int size) {
                return new BreedsBean[size];
            }
        };
    }

    public static class NameBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public NameBean(String value) {
            this.value = value;
        }

        protected NameBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<NameBean> CREATOR = new Creator<NameBean>() {
            @Override
            public NameBean createFromParcel(Parcel source) {
                return new NameBean(source);
            }

            @Override
            public NameBean[] newArray(int size) {
                return new NameBean[size];
            }
        };
    }

    public static class SexBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public SexBean(String value) {
            this.value = value;
        }

        protected SexBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<SexBean> CREATOR = new Creator<SexBean>() {
            @Override
            public SexBean createFromParcel(Parcel source) {
                return new SexBean(source);
            }

            @Override
            public SexBean[] newArray(int size) {
                return new SexBean[size];
            }
        };
    }

    public static class DescriptionBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public DescriptionBean(String value) {
            this.value = value;
        }

        protected DescriptionBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<DescriptionBean> CREATOR = new Creator<DescriptionBean>() {
            @Override
            public DescriptionBean createFromParcel(Parcel source) {
                return new DescriptionBean(source);
            }

            @Override
            public DescriptionBean[] newArray(int size) {
                return new DescriptionBean[size];
            }
        };
    }

    public static class MixBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public MixBean(String value) {
            this.value = value;
        }

        protected MixBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<MixBean> CREATOR = new Creator<MixBean>() {
            @Override
            public MixBean createFromParcel(Parcel source) {
                return new MixBean(source);
            }

            @Override
            public MixBean[] newArray(int size) {
                return new MixBean[size];
            }
        };
    }

    public static class ShelterIdBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public ShelterIdBean(String value) {
            this.value = value;
        }

        protected ShelterIdBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<ShelterIdBean> CREATOR = new Creator<ShelterIdBean>() {
            @Override
            public ShelterIdBean createFromParcel(Parcel source) {
                return new ShelterIdBean(source);
            }

            @Override
            public ShelterIdBean[] newArray(int size) {
                return new ShelterIdBean[size];
            }
        };
    }

    public static class LastUpdateBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public LastUpdateBean(String value) {
            this.value = value;
        }

        protected LastUpdateBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<LastUpdateBean> CREATOR = new Creator<LastUpdateBean>() {
            @Override
            public LastUpdateBean createFromParcel(Parcel source) {
                return new LastUpdateBean(source);
            }

            @Override
            public LastUpdateBean[] newArray(int size) {
                return new LastUpdateBean[size];
            }
        };
    }

    public static class AnimalBean implements Parcelable {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value);
        }

        public AnimalBean(String value) {
            this.value = value;
        }

        protected AnimalBean(Parcel in) {
            this.value = in.readString();
        }

        public static final Creator<AnimalBean> CREATOR = new Creator<AnimalBean>() {
            @Override
            public AnimalBean createFromParcel(Parcel source) {
                return new AnimalBean(source);
            }

            @Override
            public AnimalBean[] newArray(int size) {
                return new AnimalBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.status, flags);
        dest.writeParcelable(this.contact, flags);
        dest.writeParcelable(this.age, flags);
        dest.writeParcelable(this.size, flags);
        dest.writeParcelable(this.media, flags);
        dest.writeParcelable(this.id, flags);
        dest.writeParcelable(this.shelterPetId, flags);
        dest.writeParcelable(this.breeds, flags);
        dest.writeParcelable(this.name, flags);
        dest.writeParcelable(this.sex, flags);
        dest.writeParcelable(this.description, flags);
        dest.writeParcelable(this.mix, flags);
        dest.writeParcelable(this.shelterId, flags);
        dest.writeParcelable(this.lastUpdate, flags);
        dest.writeParcelable(this.animal, flags);
    }

    public PetBean() {
    }

    protected PetBean(Parcel in) {
        this.status = in.readParcelable(StatusBean.class.getClassLoader());
        this.contact = in.readParcelable(ContactBean.class.getClassLoader());
        this.age = in.readParcelable(AgeBean.class.getClassLoader());
        this.size = in.readParcelable(SizeBean.class.getClassLoader());
        this.media = in.readParcelable(MediaBean.class.getClassLoader());
        this.id = in.readParcelable(IdBean.class.getClassLoader());
        this.shelterPetId = in.readParcelable(ShelterPetIdBean.class.getClassLoader());
        this.breeds = in.readParcelable(BreedsBean.class.getClassLoader());
        this.name = in.readParcelable(NameBean.class.getClassLoader());
        this.sex = in.readParcelable(SexBean.class.getClassLoader());
        this.description = in.readParcelable(DescriptionBean.class.getClassLoader());
        this.mix = in.readParcelable(MixBean.class.getClassLoader());
        this.shelterId = in.readParcelable(ShelterIdBean.class.getClassLoader());
        this.lastUpdate = in.readParcelable(LastUpdateBean.class.getClassLoader());
        this.animal = in.readParcelable(AnimalBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<PetBean> CREATOR = new Parcelable.Creator<PetBean>() {
        @Override
        public PetBean createFromParcel(Parcel source) {
            return new PetBean(source);
        }

        @Override
        public PetBean[] newArray(int size) {
            return new PetBean[size];
        }
    };
}
