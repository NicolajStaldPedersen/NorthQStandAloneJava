package com.northqstandalone.maven.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.northqstandalone.maven.models.QMotionModel;
import com.northqstandalone.maven.models.QPlugModel;
import com.northqstandalone.maven.services.QMotionService;
import com.northqstandalone.maven.services.QPlugService;
import com.northqstandalone.maven.view.View;

public class QMotionController {

	private QMotionModel model;
	private View view;
	private QMotionService service;
	private String token;
	private boolean qMotionStatus;

	@Autowired
	public void setQMotionService(QMotionService service) {
		this.service = service;
	}

	@Autowired
	public void setQMotionModel(QMotionModel model) {
		this.model = model;
	}

	public void setView(View view, String token, boolean status) {
		this.view = view;
		this.token = token;
		this.qMotionStatus = status;

		setQMotionStatus(status);

		// Enable event listener
		this.view.addQMotionListener(new addQMotionListener(service, model, view));
	}

	private void setQMotionStatus(boolean status) {
		// Change view and model based on status
		if (qMotionStatus) {
			// If motion sensor is armed
			model.setArmed(true);
			view.setMotionButtonText(0);
		} else {
			// If motion sensor is disarmed
			model.setArmed(false);
			view.setMotionButtonText(1);
		}

	}

	// Action listener to receive event from view
	class addQMotionListener implements ActionListener {

		private QMotionService service;
		private QMotionModel model;
		private View view;

		public addQMotionListener(QMotionService service, QMotionModel model, View view) {
			this.service = service;
			this.model = model;
			this.view = view;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if (model.isArmed() == true) {
				service.disarmMotion(token);
				model.setArmed(false);
				view.setMotionButtonText(1);

			} else if (model.isArmed() == false) {
				service.armMotion(token);
				model.setArmed(true);
				view.setMotionButtonText(0);
			}
		}
	}
}
