package com.utils.gui_utils.objects.messages;

import org.junit.jupiter.api.Test;

import com.utils.concurrency.ThreadUtils;
import com.utils.gui.AbstractCustomApplicationTestVitesco;
import com.utils.gui_utils.GuiUtils;
import com.utils.log.messages.MessageType;
import com.utils.log.messages.Messages;

import javafx.scene.layout.StackPane;

class SplitPaneMessagesTest extends AbstractCustomApplicationTestVitesco {

	SplitPaneMessagesTest() {
		super(null);
	}

	@Test
	void testLayout() {

		final UserActionRequiredHelper userActionRequiredHelper = createUserActionRequiredHelper();
		final SplitPaneMessages splitPaneMessages = new SplitPaneMessages(userActionRequiredHelper);

		final Messages messages = createMessages();
		splitPaneMessages.setData(messages);

		GuiUtils.run(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			stackPaneContainer.getChildren().add(splitPaneMessages.getRoot());
		});
		ThreadUtils.trySleep(LONG_WAIT_TIME);
	}

	private static UserActionRequiredHelper createUserActionRequiredHelper() {

		return messageCategoryName -> {
		};
	}

	private static Messages createMessages() {

		final Messages messages = new Messages();

		messages.addMessage(MessageType.CRITICAL, 2, "Critical Message Type 1", "Critical Message 1");
		messages.addMessage(MessageType.CRITICAL, 1, "Critical Message Type 1", "Critical Message 2");
		messages.addMessage(MessageType.CRITICAL, 3, "Critical Message Type 1", "Critical Message 3");

		messages.addMessage(MessageType.CRITICAL, 1, "Critical Message Type 2", "Critical Message 1");
		messages.addMessage(MessageType.CRITICAL, 2, "Critical Message Type 2", "Critical Message 2");

		messages.addMessage(MessageType.CRITICAL, 1, "Critical Message Type 3", "Critical Message 1");
		messages.addMessage(MessageType.CRITICAL, 2, "Critical Message Type 3", "Critical Message 2");
		messages.addMessage(MessageType.CRITICAL, 3, "Critical Message Type 3", "Critical Message 3");
		messages.addMessage(MessageType.CRITICAL, 4, "Critical Message Type 3", "Critical Message 4");

		messages.addMessage(MessageType.WARNING, 1, "Warning Message Type 1", "Warning Message 1");
		messages.addMessage(MessageType.WARNING, 2, "Warning Message Type 1", "Warning Message 2");

		messages.addMessage(MessageType.WARNING, 1, "Warning Message Type 2", "Warning Message 1");
		messages.addMessage(MessageType.WARNING, 2, "Warning Message Type 2", "Warning Message 2");
		messages.addMessage(MessageType.WARNING, 3, "Warning Message Type 2", "Warning Message 3");

		messages.addMessage(MessageType.INFO, 1, "Info Message 1", "Info Message 1");
		messages.addMessage(MessageType.INFO, 2, "Info Message 2", "Info Message 2");

		return messages;
	}
}
