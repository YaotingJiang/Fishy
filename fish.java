// Assignment 5 - Problem 2
// Dan Hartman
// hartmanda
// Yaoting Jiang
// jyt

import tester.*; // The tester library
import javalib.worldimages.*; // images, like RectangleImage or OverlayImages
import javalib.funworld.*; // the abstract World class and the big-bang library
import java.awt.Color; // general colors and predefined colors
import java.util.Random;

// to represent a fish class 
class Fish {
  Posn pos;
  int radius;
  int xSpeed;
  Color color;

  // constructor one(for player fish) 
  Fish(Posn pos, int radius) {
    this.pos = pos;
    this.radius = radius;
  }
  
  // constructor two(for the background fish)
  Fish(Posn pos, int radius, Color color) {
    this.pos = pos;
    this.radius = radius;
    this.xSpeed = 10;
    this.color = color;
  }
  
  /*
  TEMPLATE in Fish class:
  ---------
  Fields:
  ... this.pos ...                                    -- Posn
  ... this.radius ...                                 -- int
  ... this.xSpeed ...                                 -- int
  ... this.color ...                                  -- Color
  Methods:
  ... this.moveFish(String) ...                   -- Fish
  ... this.image() ...                            -- WorldImage
  ... this.imageFishes(WorldScene) ...            -- WorldScene
  ... this.loopPlayerBack()...                    -- Fish
  ... this.loopBack() ...                         -- Fish
  ... this.isBiggerThanAndOverlay(Fish) ...       -- Boolean
  ... this.isSmallerThanAndOverlay(Fish) ...      -- Boolean
  ... this.grow() ...                             -- int
  Methods for fields: 
  */

  // move this fish to a new position by a given key
  Fish moveFish(String ke) {
    if (ke.equals("right")) {
      return new Fish(new Posn(this.pos.x + 5, this.pos.y), this.radius);
    }
    else if (ke.equals("left")) {
      return new Fish(new Posn(this.pos.x - 5, this.pos.y), this.radius);
    }
    else if (ke.equals("down")) {
      return new Fish(new Posn(this.pos.x, this.pos.y + 5), this.radius);
    }
    else if (ke.equals("up")) {
      return new Fish(new Posn(this.pos.x, this.pos.y - 5), this.radius);
    }
    else { 
      return this;
    }
  }
  
  
  // draw the player fish
  WorldImage image() {
    return new CircleImage(this.radius, OutlineMode.SOLID, Color.blue);
  }
  
  // draw the background fish
  WorldScene imageFishes(WorldScene background) {
    return background.placeImageXY(new CircleImage(this.radius, OutlineMode.SOLID, this.color),
        this.pos.x, this.pos.y);
  }
  
  // if the player fish is out of the top boundary, it will appear from the bottom
  // if the player fish is out of the bottom boundary, it will appear from the top
  // if the player fish is out of the left boundary, it will appear from the right
  // if the player fish is out of the right boundary, it will appear from the left
  Fish loopPlayerBack() {
    if (this.pos.x > 500 + this.radius) {
      return new Fish(new Posn(-radius, this.pos.y), this.radius);
    }
    if (this.pos.x < -this.radius) {
      return new Fish(new Posn(500 + radius, this.pos.y), this.radius);
    }
    if (this.pos.y > 500 + this.radius) {
      return new Fish(new Posn(this.pos.x, -this.radius), this.radius);
    }
    if (this.pos.y < -this.radius) {
      return new Fish(new Posn(this.pos.x, 500), this.radius);
    }
    return this;
  }
  
  // if the background fishes are out of the left boundary, they will appear from the right
  // if the background fishes are out of the right boundary, they will appear from the left
  Fish loopBack() {
    if (this.pos.x > 500 + this.radius) {
      return new Fish(new Posn(-radius, this.pos.y), this.radius, this.color);
    }
    if (this.pos.x < -this.radius) {
      return new Fish(new Posn(500 + radius, this.pos.y), this.radius, this.color);
    }
    return this;
  }

  // check if this fish touched that fish and if this fish is bigger than that fish
  boolean isBiggerThanAndOverlay(Fish that) {
    return ((Math.pow((this.pos.x - that.pos.x), 2)
        + Math.pow((this.pos.y - that.pos.y), 2)) <= Math.pow((this.radius + that.radius), 2))
        && this.radius > that.radius;
  }
  
  // check if this fish touched that fish and if this fish is smaller than that fish
  boolean isSmallerThanAndOverlay(Fish that) { 
    return ((Math.pow((this.pos.x - that.pos.x), 2)
        + Math.pow((this.pos.y - that.pos.y), 2)) <= Math.pow((this.radius + that.radius), 2))
        && this.radius <= that.radius;
  }
  
  // grow the radius of the player fish once it eats a smaller fish. 
  Fish grow() { 
    return new Fish(this.pos, this.radius + 5);
  }
}
  
 

// *************************interface**********************************************************
// to represent an interface of a list of background fishes
interface ILoFish {
  // check the size of the list of fishes
  int size();

  // make the background fishes move by changing position x
  ILoFish moveFish();

  // randomly generate a list of fishes
  ILoFish makeFishes();

  // draw a list of background fishes on world
  WorldScene image(WorldScene background);

  // if the background fishes are out of the left boundary, they will appear from the right
  // if the background fishes are out of the right boundary, they will appear from the left
  ILoFish loopBack();
  
  // check if the position of the list of fish in the range from 0 to 500
  boolean getpos();

  // if the player fish eats a smaller fish, remove that smaller fish
  // and generate a new list of background fishes
  ILoFish generateNewList(Fish that);

  // check if this fish touched that fish and if this fish is bigger than that fish
  boolean isBiggerThanHelper(Fish f); 

  // check if this fish touched that fish and if this fish is smaller than that fish
  boolean isSmallerThanHelper(Fish f);

  // determines the largest fish by checking its radius
  int maxSize();
}

// ********************************MtLo****************************************
// to represent an empty list of fish
class MtLoFish implements ILoFish {
  
  /*
  TEMPLATE in MtLoFish class:
  ---------
  Fields:
  Methods:
  ... this.size() ...                                -- int
  ... this.image() ...                               -- WorldScene
  ... this.moveFish() ...                            -- ILoFish
  ... this.makeFishes()...                           -- ILoFish
  ... this.loopBack() ...                            -- ILoFish
  ... this.getpos() ...                              -- boolean
  ... this.generateNewList(Fish)                     -- ILoFish
  ... this.isBiggerThanHelper(Fish) ...              -- Boolean
  ... this.isSmallerThanHelper(Fish) ...             -- Boolean
  ... this.maxSize() ...                             -- int
  Methods for fields: 
  */
  
  // check the size of the list of fishes
  public int size() {
    return 0;
  }

  // draw a list of background fishes on world
  public WorldScene image(WorldScene background) {
    return background;
  }

  // make the background fishes move by changing position x
  public ILoFish moveFish() {
    return this;
  }

  // randomly generate a list of fishes
  public ILoFish makeFishes() {
    return this;
  }
  
  // check if the position of the list of fish in the range from 0 to 500
  public boolean getpos() { 
    return false;
  }
  
  // if the background fishes are out of the left boundary, they will appear from the right
  // if the background fishes are out of the right boundary, they will appear from the left
  public ILoFish loopBack() {
    return this;
  }

  // if the player fish eats a smaller fish, remove that smaller fish
  // and generate a new list of background fishes
  public ILoFish generateNewList(Fish that) {
    return this;
  }

  // check if this fish touched that fish and if this fish is bigger than that fish
  public boolean isBiggerThanHelper(Fish f) {
    return false;
  }

  // check if this fish touched that fish and if this fish is smaller than that fish
  public boolean isSmallerThanHelper(Fish f) { 
    return false;
  }


  // determines the largest fish by checking its radius
  public int maxSize() { 
    return 0;
  }

}

//********************************ConsLo****************************************
// to represent a list of background fishes
class ConsLoFish implements ILoFish {
  Fish first;
  ILoFish rest;
  
  // constructor
  ConsLoFish(Fish first, ILoFish rest) {
    this.first = first;
    this.rest = rest;
  }
  
  /*
  TEMPLATE in ConsLoFish class:
  ---------
  Fields:
  ... this.first...                                 -- Fish
  ... this.rest...                                  -- ILoFish
  Methods:
  ... this.size() ...                                -- int
  ... this.image() ...                               -- WorldScene
  ... this.moveFish() ...                            -- ILoFish
  ... this.makeFishes()...                           -- ILoFish
  ... this.getpos() ...                              -- boolean
  ... this.loopBack() ...                            -- ILoFish
  ... this.generateNewList(Fish)                     -- ILoFish
  ... this.isBiggerThanHelper(Fish) ...              -- Boolean
  ... this.isSmallerThanHelper(Fish) ...             -- Boolean
  ... this.maxSize() ...                             -- int
  Methods for fields: 
  ... this.rest.size() ...                                -- int
  ... this.rest.image() ...                               -- WorldScene
  ... this.rest.moveFish() ...                            -- ILoFish
  ... this.rest.makeFishes()...                           -- ILoFish
  ... this.rest.getpos() ...                              -- boolean
  ... this.rest.loopBack() ...                            -- ILoFish
  ... this.rest.generateNewList(Fish)                     -- ILoFish
  ... this.rest.isBiggerThanHelper(Fish) ...              -- Boolean
  ... this.rest.isSmallerThanHelper(Fish) ...             -- Boolean
  ... this.rest.maxSize() ...                             -- int
  */

  // check the size of the list of fishes
  public int size() {
    return 1 + this.rest.size();
  }

  // draw a list of background fishes on world
  public WorldScene image(WorldScene background) {
    return this.rest.image(this.first.imageFishes(background));

  }

  // make the background fishes move by changing position x
  public ILoFish moveFish() {
    return new ConsLoFish(new Fish(new Posn(this.first.pos.x + this.first.xSpeed, this.first.pos.y),
        this.first.radius, this.first.color), this.rest.moveFish());
  }

  // randomly generate a list of fishes
  public ILoFish makeFishes() {
    Random r = new Random();
    Fish f1 = new Fish(new Posn(-r.nextInt(500), r.nextInt(500)), 5 + r.nextInt(35),
        new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
    if (this.rest.size() > 15) {
      return this;
    }
    else {
      return new ConsLoFish(f1, this);
    }
  }
  
  // check if the position of the list of random fish is in the range of 0 to 500 
  public boolean getpos() {
    if ((0 <= this.first.pos.x && this.first.pos.x <= 500)
        && (0 <= this.first.pos.y && this.first.pos.y <= 500)) {
      return true;
    }
    else {
      return this.rest.getpos();
    }
  }

  // if the background fishes are out of the left boundary, they will appear from the right
  // if the background fishes are out of the right boundary, they will appear from the left
  public ILoFish loopBack() {
    return new ConsLoFish(this.first.loopBack(), this.rest.loopBack());

  }

  // if the player fish eats a smaller fish, remove that smaller fish
  // and generate a new list of background fishes
  public ILoFish generateNewList(Fish that) {
    if (this.first.isSmallerThanAndOverlay(that)) {
      return this.rest;
    }
    else {
      return new ConsLoFish(this.first, this.rest.generateNewList(that));
    }
  }

  // check if this fish touched that fish and if this fish is bigger than that fish
  public boolean isBiggerThanHelper(Fish f) { 
    return this.first.isBiggerThanAndOverlay(f) || this.rest.isBiggerThanHelper(f);
  }

  // check if this fish touched that fish and if this fish is smaller than that fish
  public boolean isSmallerThanHelper(Fish f) { 
    return this.first.isSmallerThanAndOverlay(f) || this.rest.isSmallerThanHelper(f);
  }

  // determines the largest fish by checking its radius
  public int maxSize() { 
    return Math.max(this.first.radius, this.rest.maxSize());
  }

}

// *************************world************************************
// represent a world of fish
class FishyWorld extends World {
  int width = 500;
  int height = 500;
  Fish fish;
  ILoFish fishes = new MtLoFish();

  // constructor
  FishyWorld(Fish fish, ILoFish fishes) {
    super();
    this.fish = fish;
    this.fishes = fishes;
  }
  
  /*
  TEMPLATE in FishyWorld class:
  ---------
  Fields:
  ... this.width...                                 -- int
  ... this.height...                                -- int
  ... this.fish...                                  -- Fish
  ... this.fishes...                                -- ILoFish
  Methods:
  ... this.makeScene() ...                           -- WorldScene
  ... this.onKeyEvent(String) ...                    -- World
  ... this.onTick() ...                              -- World
  ... this.lastScene(String)...                      -- WorldScene 
  ... this.worldEnds() ...                           -- WorldEnd
  ... this.generateNewList(Fish)                     -- ILoFish
  Methods for fields: 
  ... this.fishes.makeScene() ...                    -- WorldScene
  ... this.fishes.onKeyEvent(String) ...             -- World
  ... this.fishes.onTick() ...                       -- World
  ... this.fishes.lastScene(String)...               -- WorldScene 
  ... this.fishes.worldEnds() ...                    -- WorldEnd
  ... this.fishes.generateNewList(Fish)              -- ILoFish
  */
  
  // Produce the image of this world by adding the fish to the background image
  public WorldScene makeScene() {
    return fishes.image(getEmptyScene().placeImageXY(fish.image(), fish.pos.x, fish.pos.y));

  }
  
  // Move the fish when the player presses a key
  public World onKeyEvent(String ke) {
    return new FishyWorld(this.fish.moveFish(ke), this.fishes);
  }
  
  // Generates a world at every tick 
  public World onTick() {
    if (fishes.isSmallerThanHelper(fish)) {
      return new FishyWorld(this.fish.loopPlayerBack().grow(),
          fishes.makeFishes().moveFish().loopBack().generateNewList(fish));
    }
    else {
      return new FishyWorld(this.fish.loopPlayerBack(), 
          fishes.makeFishes().moveFish().loopBack());
    }
  }
  
  // produce the last image of this world by adding text to the image
  public WorldScene lastScene(String s) {
    return this.makeScene().placeImageXY(new TextImage(s, 50, FontStyle.BOLD, Color.red), 250, 250);
  }
  
  // checks whether the player fish is eaten by bigger fish, 
  // or it grows to be the largest fish
  public WorldEnd worldEnds() {
    if (this.fishes.isBiggerThanHelper(fish)) {
      return new WorldEnd(true, this.lastScene("You Lose!"));
    }
    if (this.fishes.maxSize() < this.fish.radius) {
      return new WorldEnd(true, this.lastScene("You Win!"));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }
}

// represent example fish class
class ExamplesFish {
  static final int width = 500;
  static final int height = 500;
  
  // example of fishyWorld
  FishyWorld w1 = new FishyWorld(new Fish(new Posn(width / 2, height / 2), 15),
      new ConsLoFish(new Fish(new Posn(100, 100), 30, Color.red), new MtLoFish()));
  FishyWorld w2 = new FishyWorld(new Fish(new Posn(width / 2, height / 2), 55),
      new ConsLoFish(new Fish(new Posn(100, 100), 20, Color.red), new MtLoFish()));
  FishyWorld w3 = new FishyWorld(new Fish(new Posn(100, 100), 5),
      new ConsLoFish(new Fish(new Posn(100, 100), 20, Color.red), new MtLoFish()));
  FishyWorld w4 = new FishyWorld(new Fish(new Posn(100, 100), 15),
      new ConsLoFish(new Fish(new Posn(100, 100), 10, Color.red), new MtLoFish()));
  
  boolean runAnimation = this.w1.bigBang(width, height, 0.3);
  
  // examples of the player fish
  Fish pf1 = new Fish(new Posn(250, 250), 15);
  Fish pf2 = new Fish(new Posn(600, 10), 25);
  Fish pf3 = new Fish(new Posn(-31, 80), 30);
  Fish pf4 = new Fish(new Posn(25, 550), 40);
  Fish pf5 = new Fish(new Posn(250, -28), 10);
  
  // examples of the background fish
  Fish f2 = new Fish(new Posn(10, 150), 10, Color.blue);
  Fish f3 = new Fish(new Posn(10, 80), 35, Color.yellow);
  Fish f4 = new Fish(new Posn(250, 250), 5, Color.gray);
  Fish f5 = new Fish(new Posn(10, 150), 10, Color.green);
  Fish f6 = new Fish(new Posn(10, 80), 32, Color.black);
  Fish f7 = new Fish(new Posn(250, 250), 18, Color.pink);
  Fish f8 = new Fish(new Posn(600, 10), 25, Color.yellow);
  Fish f9 = new Fish(new Posn(-31, 80), 30, Color.black);
  Fish f10 = new Fish(new Posn(10, 151), 12, Color.black);
  
  // examples of lists of fishes 
  ILoFish lf1 = new MtLoFish();
  ILoFish lf2 = new ConsLoFish(this.f2, this.lf1);
  ILoFish lf3 = new ConsLoFish(this.f3, new ConsLoFish(this.f4, this.lf1));
  ILoFish lf4 = new ConsLoFish(this.f4, new ConsLoFish(this.f5, new ConsLoFish(this.f7, this.lf1)));
  ILoFish lf5 = new ConsLoFish(this.f5, new ConsLoFish(this.f6, this.lf4));
  ILoFish lf6 = new ConsLoFish(this.f6, new ConsLoFish(this.f4, this.lf4));
  ILoFish lf7 = new ConsLoFish(this.f9, this.lf1);
  ILoFish lf8 = new ConsLoFish(new Fish(new Posn(800, 650), 10, Color.red), this.lf7);

  // Tests 
  boolean test(Tester t) {
    return
    // tests for moveFish method
    t.checkExpect(this.f2.moveFish("right"), new Fish(new Posn(15, 150), 10, Color.blue))
        && t.checkExpect(this.f3.moveFish("left"), new Fish(new Posn(5, 80), 35, Color.yellow))
        && t.checkExpect(this.f4.moveFish("up"), new Fish(new Posn(250, 245), 5, Color.gray))
        && t.checkExpect(this.f5.moveFish("down"), new Fish(new Posn(10, 155), 10, Color.green))
        && t.checkExpect(this.f6.moveFish("right"), new Fish(new Posn(15, 80), 32, Color.black))
        && t.checkExpect(this.f7.moveFish("left"), new Fish(new Posn(245, 250), 18, Color.pink))

        // tests for loopPalyerBack method
        && t.checkExpect(this.pf1.loopPlayerBack(), this.pf1)
        && t.checkExpect(this.pf2.loopPlayerBack(), new Fish(new Posn(-25, 10), 25))
        && t.checkExpect(this.pf3.loopPlayerBack(), new Fish(new Posn(530, 80), 30))
        && t.checkExpect(this.pf4.loopPlayerBack(), new Fish(new Posn(25, -40), 45))
        && t.checkExpect(this.pf5.loopPlayerBack(), new Fish(new Posn(250, 500), 10))

        // tests for loop
        && t.checkExpect(this.f2.loopBack(), this.f2)
        && t.checkExpect(this.f7.loopBack(), new Fish(new Posn(250, 250), 25, Color.pink))
        && t.checkExpect(this.f8.loopBack(), new Fish(new Posn(-25, 10), 30, Color.black))

        // tests for isBiggerThanAndOverlay
        && t.checkExpect(this.f2.isBiggerThanAndOverlay(pf1), false)
        && t.checkExpect(this.f7.isBiggerThanAndOverlay(pf2), false)
        && t.checkExpect(this.f8.isBiggerThanAndOverlay(pf3), false)
        && t.checkExpect(this.f5.isBiggerThanAndOverlay(f10), false)
        && t.checkExpect(this.pf1.isBiggerThanAndOverlay(f4), true)
        && t.checkExpect(this.f5.isBiggerThanAndOverlay(f6), false)

        // tests for isSmallerThanAndOverlay method
        && t.checkExpect(this.f2.isSmallerThanAndOverlay(pf1), false)
        && t.checkExpect(this.f7.isSmallerThanAndOverlay(pf2), false)
        && t.checkExpect(this.f8.isSmallerThanAndOverlay(pf3), false)
        && t.checkExpect(this.f5.isSmallerThanAndOverlay(f10), true)
        && t.checkExpect(this.pf1.isSmallerThanAndOverlay(f4), false)
        && t.checkExpect(this.f5.isSmallerThanAndOverlay(f6), false)

        // tests for size method
        && t.checkExpect(this.lf1.size(), 0) && t.checkExpect(this.lf2.size(), 1)
        && t.checkExpect(this.lf3.size(), 2) && t.checkExpect(this.lf4.size(), 3)
        && t.checkExpect(this.lf5.size(), 5) && t.checkExpect(this.lf6.size(), 5)

        // tests for moveFish method
        && t.checkExpect(this.lf1.moveFish(), this.lf1)
        && t.checkExpect(this.lf2.moveFish(),
            new ConsLoFish(new Fish(new Posn(20, 150), 10, Color.blue), this.lf1))
        && t.checkExpect(this.lf3.moveFish(), new ConsLoFish(
            new Fish(new Posn(20, 80), 10, Color.yellow),
            new ConsLoFish(new Fish(new Posn(260, 250), 5, new Color(128, 128, 128)), this.lf1)))

        // tests for makeFishes method & check their position
        && t.checkExpect(lf1.makeFishes(), this.lf1)
        && t.checkExpect(lf2.getpos(), true)
        && t.checkExpect(lf3.getpos(), true)
        && t.checkExpect(lf4.getpos(), true)
        && t.checkExpect(lf5.getpos(), true)
        && t.checkExpect(lf6.getpos(), true)
        && t.checkExpect(lf7.getpos(), false)
        && t.checkExpect(lf8.getpos(), false)

        // tests for loopBack method in ILoFish
        && t.checkExpect(this.lf1.loopBack(), this.lf1)
        && t.checkExpect(this.lf2.loopBack(),
            new ConsLoFish(new Fish(new Posn(10, 150), 10, Color.blue), this.lf1))
        && t.checkExpect(this.lf3.loopBack(), new ConsLoFish(
            new Fish(new Posn(10, 80), 35, Color.black),
            new ConsLoFish(new Fish(new Posn(250, 250), 5, new Color(128, 128, 128)), this.lf1)))

        // tests for generateNewList method
        && t.checkExpect(this.lf1.generateNewList(f10), this.lf1)
        && t.checkExpect(this.lf2.generateNewList(f2), this.lf1)
        && t.checkExpect(this.lf3.generateNewList(f3),
            new ConsLoFish(new Fish(new Posn(250, 250), 5, new Color(128, 128, 128)), this.lf1))
        && t.checkExpect(this.lf4.generateNewList(f4),
            new ConsLoFish(new Fish(new Posn(10, 150), 10, new Color(0, 255, 0)),
                new ConsLoFish(new Fish(new Posn(250, 250), 18, new Color(255, 175, 175)),
                    this.lf1)))
        && t.checkExpect(this.lf5.generateNewList(f5),
            new ConsLoFish(new Fish(new Posn(10, 80), 32, new Color(0, 0, 0)),
                new ConsLoFish(new Fish(new Posn(250, 250), 5, new Color(128, 128, 128)),
                    new ConsLoFish(new Fish(new Posn(10, 150), 10, new Color(0, 255, 0)),
                        new ConsLoFish(new Fish(new Posn(250, 250), 18, new Color(255, 175, 175)),
                            this.lf1)))))
        && t.checkExpect(this.lf6.generateNewList(f6),
            new ConsLoFish(new Fish(new Posn(250, 250), 5, new Color(128, 128, 128)),
                new ConsLoFish(new Fish(new Posn(10, 150), 10, new Color(0, 255, 0)),
                    new ConsLoFish(new Fish(new Posn(250, 250), 18, new Color(255, 175, 175)),
                        this.lf1))))

        // tests for isBiggerThanHelper method
        && t.checkExpect(this.lf1.isBiggerThanHelper(pf1), false)
        && t.checkExpect(this.lf2.isBiggerThanHelper(pf2), false)
        && t.checkExpect(this.lf3.isBiggerThanHelper(pf3), true)
        && t.checkExpect(this.lf4.isBiggerThanHelper(f10), false)
        && t.checkExpect(this.lf5.isBiggerThanHelper(f4), true)
        && t.checkExpect(this.lf5.isBiggerThanHelper(f6), false)

        // tests for isSmallerThanHelper method
        && t.checkExpect(this.lf1.isSmallerThanHelper(pf1), false)
        && t.checkExpect(this.lf2.isSmallerThanHelper(pf2), false)
        && t.checkExpect(this.lf3.isSmallerThanHelper(pf3), false)
        && t.checkExpect(this.lf4.isSmallerThanHelper(f10), true)
        && t.checkExpect(this.lf5.isSmallerThanHelper(f4), true)
        && t.checkExpect(this.lf6.isSmallerThanHelper(f6), true)

        // tests for maxSize method
        && t.checkExpect(this.lf1.maxSize(), 0) && t.checkExpect(this.lf2.maxSize(), 10)
        && t.checkExpect(this.lf3.maxSize(), 35) && t.checkExpect(this.lf4.maxSize(), 18)
        && t.checkExpect(this.lf5.maxSize(), 32) && t.checkExpect(this.lf6.maxSize(), 32)

        // tests for onkeyEvent method in FishyWorld
        && t.checkExpect(this.w1.onKeyEvent("left"),
            new FishyWorld(new Fish(new Posn(width / 2, height / 2), 15),
                new ConsLoFish(new Fish(new Posn(95, 100), 30, Color.red), new MtLoFish())))
        && t.checkExpect(this.w1.onKeyEvent("right"),
            new FishyWorld(new Fish(new Posn(width / 2, height / 2), 15),
                new ConsLoFish(new Fish(new Posn(105, 100), 30, Color.red), new MtLoFish())))
        && t.checkExpect(this.w1.onKeyEvent("up"),
            new FishyWorld(new Fish(new Posn(width / 2, height / 2), 15),
                new ConsLoFish(new Fish(new Posn(100, 95), 30, Color.red), new MtLoFish())))
        && t.checkExpect(this.w1.onKeyEvent("left"),
            new FishyWorld(new Fish(new Posn(width / 2, height / 2), 15),
                new ConsLoFish(new Fish(new Posn(100, 105), 30, Color.red), new MtLoFish())))

        // tests for onTick method in FishyWorld
        && t.checkExpect(this.w1.onTick(),
            new FishyWorld(new Fish(new Posn(width / 2, height / 2), 15).loopPlayerBack(),
                new ConsLoFish(new Fish(new Posn(100, 100), 30, Color.red), new MtLoFish())
                    .makeFishes().moveFish().loopBack()))
        && t.checkExpect(this.w4.onTick(), new FishyWorld(
            new Fish(new Posn(100, 100), 15).loopPlayerBack().grow(),
            new ConsLoFish(new Fish(new Posn(100, 100), 10, Color.red), 
                new MtLoFish()).makeFishes()
            .moveFish().loopBack()
            .generateNewList(new Fish(new Posn(100, 100), 15))))
        

        // tests for WorldEnds method in FishyWorld
        && t.checkExpect(this.w1.worldEnds(), new WorldEnd(false, this.w1.makeScene()))
        && t.checkExpect(this.w2.worldEnds(), new WorldEnd(true, this.w2.lastScene("You Win!")))
        && t.checkExpect(this.w3.worldEnds(), new WorldEnd(true, this.w3.lastScene("You Lose!")));
  }
}
