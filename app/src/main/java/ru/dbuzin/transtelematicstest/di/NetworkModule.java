package ru.dbuzin.transtelematicstest.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.dbuzin.transtelematicstest.api.FriendsApi;
import ru.dbuzin.transtelematicstest.api.SettingsApi;

@Module
public class NetworkModule {

    String baseUrl;

    public NetworkModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder
                .setLenient()
                .create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    @Provides
    @Singleton
    FriendsApi provideUsersApi(Retrofit retrofit){
        return retrofit.create(FriendsApi.class);
    }

    @Provides
    @Singleton
    SettingsApi provideSettingsApi(Retrofit retrofit){
        return retrofit.create(SettingsApi.class);
    }
}
