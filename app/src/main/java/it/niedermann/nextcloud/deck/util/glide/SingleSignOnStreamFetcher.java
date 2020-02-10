package it.niedermann.nextcloud.deck.util.glide;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.nextcloud.android.sso.aidl.NextcloudRequest;
import com.nextcloud.android.sso.api.NextcloudAPI;
import com.nextcloud.android.sso.api.Response;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.niedermann.nextcloud.deck.DeckLog;


/**
 * Fetches an {@link InputStream} using the okhttp library.
 */
public class SingleSignOnStreamFetcher implements DataFetcher<InputStream> {
    private static final String TAG = SingleSignOnStreamFetcher.class.getCanonicalName();

    private final NextcloudAPI client;
    private final GlideUrl url;

    // Public API.
    @SuppressWarnings("WeakerAccess")
    public SingleSignOnStreamFetcher(NextcloudAPI client, GlideUrl url) {
        this.client = client;
        this.url = url;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull final DataCallback<? super InputStream> callback) {
        NextcloudRequest.Builder requestBuilder = new NextcloudRequest.Builder()
                .setMethod("GET")
                .setUrl(url.toStringUrl());
        Map<String, List<String>> header = new HashMap<>();
        for (Map.Entry<String, String> headerEntry : url.getHeaders().entrySet()) {
            header.put(headerEntry.getKey(), Collections.singletonList(headerEntry.getValue()));
        }
        requestBuilder.setHeader(header);
        NextcloudRequest nextcloudRequest = requestBuilder.build();
        DeckLog.log(nextcloudRequest.toString());
        try {
            Response response = client.performNetworkRequestV2(nextcloudRequest);
            callback.onDataReady(response.getBody());
        } catch (Exception e) {
            callback.onLoadFailed(e);
        }

    }

    @Override
    public void cleanup() {

    }

    @Override
    public void cancel() {

    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
