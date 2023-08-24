package com.rmlogsClient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.*;

import java.io.File;
import java.util.*;


@Environment(EnvType.CLIENT)
public class RmlogsScreen extends Screen {

    public String listFiles(File dir) {
        ArrayList<String> fileList = new ArrayList<>();
        Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(File::isFile)
                .forEach(file -> fileList.add(String.valueOf(file)));
        return String.valueOf(fileList);
    }
    private static File getOperatingSysLogDir() {
        String systemUser = System.getProperty("user.name");
        String OperatingSystem = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        System.out.println(OperatingSystem);
        if (OperatingSystem.contains("nix") || OperatingSystem.contains("nux") || OperatingSystem.contains("aix")) {
            return new File("/home/"+systemUser+"/.minecraft/logs");
        } else if (OperatingSystem.contains("win")) {
            return new File("C:\\Users\\"+systemUser+"\\AppData\\Roaming\\.minecraft\\logs");
        } else {
            System.out.println("What OS are you using?");
            return null;
        }
    }

    private static final File currentlogDir = getOperatingSysLogDir();
    private final Screen parent;
    protected RmlogsScreen(Screen parent) {
        super(Text.literal("Viewing log files"));
        this.parent = parent;
    }
    public ButtonWidget clearAllLogs;

    public static void delAllFilesInDir(File dir) {
        Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(File::isFile)
                .forEach(file -> file.delete());
    }

    @Override
    protected void init() {
        clearAllLogs = ButtonWidget.builder(Text.literal("Clear all logs"), button -> {
                    try {
                        delAllFilesInDir(Objects.requireNonNull(getOperatingSysLogDir()));
                    } catch (NullPointerException npe) {
                        System.err.println("Failed to delete logs as provided a null pointer exception." + npe.getCause());
                    }
                })
                .dimensions(width / 2 - 205, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("Remove remaining log files.")))
                .build();

        addDrawableChild(clearAllLogs);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        assert currentlogDir != null;
        final MultilineText multilineText = MultilineText.create(textRenderer, Text.literal(listFiles(currentlogDir)), width - 20);
        multilineText.drawWithShadow(context, 40, 56, 10, 0x878787);
    }

    @Override
    public void close() {
        assert client != null;
        client.setScreen(parent);
    }
}
