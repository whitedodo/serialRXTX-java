/*
 * 	2017. 10. 21
 * 	KeybindingController.java
 * 	Created By D.Y. Jung
 */

package Libraries;

import Main.GUI;

public class KeybindingController {
	GUI window = null;

	public KeybindingController(GUI window) {
		this.window = window;
	}


	public void toggleControls() {
		
		if (window.communicator.getConnected() == true) {
			
			window.btnDisconnect.setEnabled(true);
			window.btnConnect.setEnabled(false);
			window.cmbPort.setEnabled(false);
			
			/*
			window.RSlider.setEnabled(true);
			window.GSlider.setEnabled(true);
			window.BSlider.setEnabled(true);
			window.IntensitySlider.setEnabled(true);
			*/
		}
		
		else {
			window.btnDisconnect.setEnabled(false);
			window.btnConnect.setEnabled(true);
			window.cmbPort.setEnabled(true);

			/*
			window.RSlider.setEnabled(false);
			window.GSlider.setEnabled(false);
			window.BSlider.setEnabled(false);
			window.IntensitySlider.setEnabled(false);
			*/
		}
	}

}