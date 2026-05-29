package dev.scx.app.x._util;

import dev.scx.io.ByteInput;
import dev.scx.io.ScxIO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.HexFormat;
import java.util.regex.Pattern;

import static java.nio.file.StandardOpenOption.*;

/// 文件 操作类
///
/// @author scx567888
/// @version 0.0.1
public final class FileUtils {


    private static final HexFormat HEX_FORMAT = HexFormat.of().withUpperCase();

    /// 删除文件或文件夹
    /// excludeRoot 用来 排除根目录 (删除文件为 "文件" 时无效, "目录" 时有效)
    /// 比如 未使用此选项调用 delete("/user/test") 文件夹 则 test 文件夹会被删除
    /// 若使用此选项则 会清空 test 下所有文件 test 目录则会保留
    ///
    /// @param start       起始目录
    /// @param excludeRoot 实现清空文件夹的效果
    /// @throws IOException e
    public static void delete(Path start, boolean excludeRoot) throws IOException {
        var visitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                var dontNeedDelete = excludeRoot && Files.isSameFile(start, dir);
                if (!dontNeedDelete) {
                    Files.delete(dir);
                }
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            //如果文件已经不存在了 我们就当它已经被删除了
            Files.walkFileTree(start, visitor);
        } catch (NoSuchFileException ignore) {

        }
    }

    public static void delete(Path start) throws IOException {
        delete(start, false);
    }

    /// 本质上就是调用 [#move(Path,Path,CopyOption...)] ,但是在之前会创建不存在的父目录
    ///
    /// @param source  a
    /// @param target  a
    /// @param options a
    /// @throws IOException a
    public static void move(Path source, Path target, CopyOption... options) throws IOException {
        try {
            Files.move(source, target, options);
        } catch (NoSuchFileException e) {
            Files.createDirectories(target.getParent());
            Files.move(source, target, options);
        }
    }

    /// 本质上就是调用 [#copy(Path,Path,CopyOption...)] ,但是在之前会创建不存在的父目录
    ///
    /// @param source  a
    /// @param target  a
    /// @param options a
    /// @throws IOException a
    public static void copy(Path source, Path target, CopyOption... options) throws IOException {
        try {
            Files.copy(source, target, options);
        } catch (NoSuchFileException e) {
            Files.createDirectories(target.getParent());
            Files.copy(source, target, options);
        }
    }

    /// 本质上就是调用 [#write(Path,byte[],OpenOption...)] ,但是在之前会创建不存在的父目录
    ///
    /// @param path    a
    /// @param bytes   a
    /// @param options a
    /// @throws IOException a
    public static void write(Path path, byte[] bytes, OpenOption... options) throws IOException {
        try {
            Files.write(path, bytes, options);
        } catch (NoSuchFileException e) {
            Files.createDirectories(path.getParent());
            Files.write(path, bytes, options);
        }
    }

    public static String getHeadAsHex(String filePath, int length) throws IOException {
        return HEX_FORMAT.formatHex(getHead(filePath, length));
    }

    public static byte[] getHead(String filePath, int length) throws IOException {
        try (var accessFile = new RandomAccessFile(filePath, "r")) {
            var headBytes = new byte[length];
            accessFile.read(headBytes);
            return headBytes;
        }
    }

    /// 获取文件名
    ///
    /// @param path a
    /// @return a
    public static String getFileName(String path) {
        return new File(path).getName();
    }

    /// 获取拓展名 (不包括 . ) 例 : "cat.png" 会获得 "png"
    ///
    /// @param path a [String] object
    /// @return a [String] object
    public static String getExtension(String path) {
        var fileName = getFileName(path);
        var dotIndex = fileName.lastIndexOf('.');
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }

    /// 获取文件名 (不包括拓展名 ) 例 : "cat.png" 会获得 "cat"
    ///
    /// @param path a [String] object
    /// @return a [String] object
    public static String getFileNameWithoutExtension(String path) {
        var fileName = getFileName(path);
        var dotIndex = fileName.lastIndexOf('.');
        return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
    }





    public static void merge(Path source, Path target) throws IOException {
        try (var in = FileChannel.open(target, READ)) {
            try (var out = FileChannel.open(source, APPEND, CREATE, SYNC, WRITE)) {
                in.transferTo(0, in.size(), out);
            } catch (NoSuchFileException e) {
                Files.createDirectories(source.getParent());
                try (var out = FileChannel.open(source, APPEND, CREATE, SYNC, WRITE)) {
                    in.transferTo(0, in.size(), out);
                }
            }
        }
    }

    public static void appendToFile(Path target, InputStream appendContent) throws IOException {
        try (var in = appendContent) {
            try (var out = Files.newOutputStream(target, APPEND, CREATE, SYNC, WRITE)) {
                in.transferTo(out);
            } catch (NoSuchFileException e) {
                Files.createDirectories(target.getParent());
                try (var out = Files.newOutputStream(target, APPEND, CREATE, SYNC, WRITE)) {
                    in.transferTo(out);
                }
            }
        }
    }

    public static void appendToFile(Path target, ByteInput byteInput) throws IOException {
        try (var in = byteInput) {
            try (var out = Files.newOutputStream(target, APPEND, CREATE, SYNC, WRITE)) {
                ScxIO.transferToAll(byteInput,out);
            } catch (NoSuchFileException e) {
                Files.createDirectories(target.getParent());
                try (var out = Files.newOutputStream(target, APPEND, CREATE, SYNC, WRITE)) {
                    ScxIO.transferToAll(byteInput,out);
                }
            }
        }
    }

}
