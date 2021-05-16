package com.utils.gui_utils.charts.division;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.utils.concurrency.ThreadUtils;
import com.utils.gui.AbstractCustomApplicationTestVitesco;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.charts.division.data.PercentageDivisionElement;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

class VBoxPercentageDivisionTest extends AbstractCustomApplicationTestVitesco {

	VBoxPercentageDivisionTest() {
		super(null);
	}

	@Test
	void testLayout() {

		final List<PercentageDivisionElement> elements = new ArrayList<>();
		elements.add(new PercentageDivisionElement("EL1", 0.2, Color.RED));
		elements.add(new PercentageDivisionElement("EL2", 0.1, Color.ORANGE));
		elements.add(new PercentageDivisionElement("EL3", 0.2, Color.YELLOW));
		elements.add(new PercentageDivisionElement("EL2", 0.2, Color.GREEN));
		elements.add(new PercentageDivisionElement("EL3", 0.1, Color.BLUE));
		elements.add(new PercentageDivisionElement("EL4", 0.2, Color.PURPLE));
		final VBoxPercentageDivision vBoxPercentageDivision = new VBoxPercentageDivision(elements);

		GuiUtils.run(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			stackPaneContainer.getChildren().add(vBoxPercentageDivision.getRoot());
		});
		ThreadUtils.trySleep(LONG_WAIT_TIME);
	}
}
