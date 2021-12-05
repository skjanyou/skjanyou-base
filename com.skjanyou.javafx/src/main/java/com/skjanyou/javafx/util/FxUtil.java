package com.skjanyou.javafx.util;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FxUtil {
	public void snapshot( File file,Node node ) throws IOException {
		// 1 构造快照参数
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);// 设置透明背景或其他颜色

		// 2 生成快照，保存到文件
		WritableImage image = node.snapshot(params, null);
		ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	}
}
