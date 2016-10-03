package se.doverfelt.thegame.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import se.doverfelt.thegame.TheGame;
import se.doverfelt.thegame.net.packet.Packet4KeyPressed;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by rickard on 2016-09-30.
 */
public class InputManager {

    private final TheGame theGame;

    private HashMap<Integer, Event> keys = new HashMap<Integer, Event>();
    private HashMap<Integer, Event> buttons = new HashMap<Integer, Event>();
    private Controller controller;

    public InputManager(TheGame theGame) {
        this.theGame = theGame;
    }

    public void addKey(int key, Event event) {
        keys.put(key, event);
    }

    public void poll() throws IOException {
        for (int key : keys.keySet()) {
            if (Gdx.input.isKeyJustPressed(key)) {
                Packet4KeyPressed keyPressed = new Packet4KeyPressed();
                keyPressed.event = keys.get(key);
                keyPressed.ctrl = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
                keyPressed.alt = Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);
                keyPressed.shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
                theGame.client.send(keyPressed);
            }
        }
        for (int button : buttons.keySet()) {
            if (Gdx.input.isButtonPressed(button)) {
                Packet4KeyPressed keyPressed = new Packet4KeyPressed();
                keyPressed.event = buttons.get(button);
                theGame.client.send(keyPressed);
            }
        }
    }

    private void sendAction(Event event) throws IOException {
        Packet4KeyPressed keyPressed = new Packet4KeyPressed();
        keyPressed.event = event;
        theGame.client.send(keyPressed);
    }

    public void setController(Controller controller) {
        this.controller = controller;
        this.controller.addListener(new ControllerAdapter() {
            @Override
            public boolean buttonDown(Controller controller, int buttonIndex) {
                Gdx.app.log("Controller", "Button - " + buttonIndex);
                if (buttonIndex == 0) {
                    try {
                        sendAction(Event.JUMP);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (buttonIndex == 2){
                    try {
                        sendAction(Event.FIRE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (buttonIndex == 3) {
                    try {
                        sendAction(Event.SECONDARY_FIRE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return super.buttonDown(controller, buttonIndex);
            }

            @Override
            public boolean axisMoved(Controller controller, int axisIndex, float value) {
                Gdx.app.log("Controller", "Axis #" + axisIndex + " - " + value);
                if (axisIndex == 1) {
                    if (value < -0.1f) {
                        try {
                            sendAction(Event.WALK_LEFT);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (value > 0.1f) {
                        try {
                            sendAction(Event.WALK_RIGHT);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return super.axisMoved(controller, axisIndex, value);
            }
        });
    }


    public void addButton(int button, Event event) {
        buttons.put(button, event);
    }

    public enum Event {
        WALK_LEFT, WALK_RIGHT, JUMP, FIRE, SECONDARY_FIRE
    }
}