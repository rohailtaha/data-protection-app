package dataProtection;

public class UpdateProgressTask implements Runnable
{
	private static int currentProgress;
	private final int maxValue;
	private final String taskName;
	
	UpdateProgressTask(int currProgress, int maxValue, String taskName) 
	{
		currentProgress = currProgress;
		this.maxValue = maxValue;
		this.taskName = taskName;
		Main.gui.progressBar.setMaximum(maxValue);
		Main.gui.progressBar.setValue(currentProgress);
	}
	
	public void run()
	{
		while(currentProgress<maxValue) {
			try {
				if(Main.gui.progressFrame.isVisible()) {
					Main.gui.progressBar.setValue(getCurrentProgress());
					Thread.sleep(1000);
				}
				else {  // the task was cancelled while in progress
					Main.gui.enableMainActionButtons();
					return;
				}
			}
			catch(InterruptedException exception) {
				Thread.currentThread().interrupt();
			}
		}
		Main.gui.progressFrame.setVisible(false);
		Main.gui.enableMainActionButtons();
		// Make the first letter of 'taskName' capitalized. (e.g. change 'encryption' to Encryption )
		String _taskName = taskName.substring(0, 1).toUpperCase() + taskName.substring(1);
		Messages.showTaskCompletionMessage(_taskName);
	}

	static void setCurrentProgress(int filesSizeAdded) {
		currentProgress += filesSizeAdded;
	}

	int getCurrentProgress()
	{
		return currentProgress;
	}	
}
