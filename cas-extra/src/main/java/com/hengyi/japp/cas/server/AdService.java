package com.hengyi.japp.cas.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Singleton
public final class AdService {
    private final String address;
    private final int port;

    @Inject
    private AdService(@Named("ad.address") String address, @Named("ad.port") int port) {
        this.address = address;
        this.port = port;
    }

    public Completable change(String username, String oldpassword, String newpassword) {
        return Single.fromCallable(() -> changePassword(username, oldpassword, newpassword))
                .flatMapCompletable(result -> {
                    if (result == null) {
                        return Completable.error(new RuntimeException());
                    }
                    if (result.length() < 1) {
                        return Completable.error(new RuntimeException());
                    }
                    if (result.charAt(0) != '0') {
                        return Completable.error(new RuntimeException());
                    }
                    return Completable.complete();
                });
    }

    @SneakyThrows
    private String changePassword(String username, String oldpassword, String newpassword) {
        @Cleanup Socket socket = new Socket(address, port);
        @Cleanup PrintWriter pwr = new PrintWriter(socket.getOutputStream());
        @Cleanup InputStreamReader in = new InputStreamReader(socket.getInputStream());
        @Cleanup BufferedReader read = new BufferedReader(in);
        // 获取提示，然后输入命令
        read.readLine();
        final String command = String.join(" ", "changepassword", username, oldpassword, newpassword);
        pwr.print(command + "\n\r");
        pwr.flush();
        return read.readLine();
    }

}
