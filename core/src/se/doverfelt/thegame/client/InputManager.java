package se.doverfelt.thegame.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import se.doverfelt.thegame.TheGame;
import se.doverfelt.thegame.net.packet.Packet4KeyPressed;
import se.doverfelt.thegame.net.packet.Packet6MouseMoved;

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
    private float lastMouseX = 0, lastMouseY = 0;
    private float lXDiff, lYDiff, rXDiff, rYDiff;

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

        if (controller.getAxis(3) < -0.3 || controller.getAxis(3) > 0.3) {
            Packet6MouseMoved packet = new Packet6MouseMoved();
            lastMouseX += Math.max(1, lastMouseX)*(controller.getAxis(3) - rXDiff)*Gdx.graphics.getDeltaTime();
            packet.x = (int) lastMouseX;
            packet.y = (int) lastMouseY;
            try {
                theGame.client.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (controller.getAxis(2) < -0.3 || controller.getAxis(2) > 0.3) {
            Packet6MouseMoved packet = new Packet6MouseMoved();
            lastMouseY -= Math.max(1, lastMouseY)*(controller.getAxis(2) - rYDiff)*Gdx.graphics.getDeltaTime();
            packet.x = (int) lastMouseX;
            packet.y = (int) lastMouseY;
            theGame.client.send(packet);
        }

        if (Gdx.input.getDeltaX() != 0 && Gdx.input.getDeltaY() != 0) {
            Packet6MouseMoved packet = new Packet6MouseMoved();
            packet.x = Gdx.input.getX();
            packet.y = Gdx.input.getY();
            lastMouseX = Gdx.input.getX();
            lastMouseY = Gdx.input.getY();
            theGame.client.send(packet);
        }

    }

    private void sendAction(Event event) throws IOException {
        Packet4KeyPressed keyPressed = new Packet4KeyPressed();
        keyPressed.event = event;
        theGame.client.send(keyPressed);
    }

    public void setController(Controller controller) {
        this.controller = controller;
        lXDiff = controller.getAxis(1);
        lYDiff = controller.getAxis(0);
        rXDiff = controller.getAxis(3);
        rYDiff = controller.getAxis(2);
        this.controller.addListener(new ControllerAdapter() {
            @Override
            public boolean buttonDown(Controller controller, int buttonIndex) {
                Gdx.app.error("Controller", "Button - " + buttonIndex);
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
                Gdx.app.error("Controller", "Axis #" + axisIndex + " - " + value);
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