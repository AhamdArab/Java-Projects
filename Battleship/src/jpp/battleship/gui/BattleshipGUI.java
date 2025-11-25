package jpp.battleship.gui;

import jpp.battleship.model.Alignment;
import jpp.battleship.model.Coordinate;
import jpp.battleship.model.ShipClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

public class BattleshipGUI extends JFrame {
    JTextField nameField;
    JRadioButton easyButton, mediumButton, hardButton;
    JCheckBox goFirstCheckBox;
    JButton startButton;
    JLabel draggedShip;
    int draggedShipSize;
    JButton[][] playerCells, computerCells;
    BufferedImage draggedShipImage;
    boolean[][] usedCells = new boolean[10][10];
    JPanel shipPanel;
    Alignment alignment = Alignment.VERTICAL;
    int shipsPlaced = 0;
    int totalShips = 5;
    JPanel computerBoardPanel;
    boolean[][] computerShipCells = new boolean[10][10];
    boolean playerTurn = true;
    int playerHits = 0;
    int computerHits = 0;
    int totalShipFields;
    JLabel winnerLabel;
    List<Coordinate> allCoordinates = new ArrayList<>();
    Random random = new Random();
    boolean[][] playerClickedCells = new boolean[10][10];
    List<JLabel> shipLabels = new ArrayList<>();
    Map<JLabel, Image> shipOriginalImages = new HashMap<>();
    JPanel playerShipsPanel = new JPanel(new FlowLayout());
    JPanel computerShipsPanel = new JPanel(new FlowLayout());
    List<JLabel> playerShipLabels = new ArrayList<>();
    List<JLabel> computerShipLabels = new ArrayList<>();
    Map<ShipClass, Integer> playerShipHits = new HashMap<>();
    List<Map.Entry<ShipClass, List<Coordinate>>> playerShips = new ArrayList<>();
    List<Map.Entry<ShipClass, List<Coordinate>>> computerShips = new ArrayList<>();
    Map<ShipClass, Integer> computerShipHits = new HashMap<>();
    Map<ShipClass, List<Coordinate>> computerShip = new HashMap<>();
    boolean[][] computerClickedCells = new boolean[10][10];
    Set<ShipClass> destroyedShips = new HashSet<>();

    public BattleshipGUI() {
        setTitle("Battleship");
        setSize(800, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        totalShipFields = calculateTotalShipFields();
        setupTopPanel();
        setupBoardPanel();
        setupShipPanel();
        setupComputerBoardPanel();
        setupEventListeners();

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BattleshipGUI::new);
    }

    private int calculateTotalShipFields(){
        int total = 0;
        for (ShipClass shipClass: ShipClass.values()) {
            total += shipClass.length();
        }
        return total;
    }
    private void setupTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nameLabel = new JLabel("Write your Name: ");
        nameLabel.setForeground(Color.GRAY);
        nameField = new JTextField(15);
        nameField.setForeground(Color.BLACK);
        nameField.setHorizontalAlignment(SwingConstants.CENTER);

        namePanel.add(nameLabel);
        namePanel.add(nameField);
        topPanel.add(namePanel, BorderLayout.NORTH);

        JPanel difficultyPanel = new JPanel();
        difficultyPanel.add(new JLabel("Difficulty: "));

        ButtonGroup difficultyGroup = new ButtonGroup();
        easyButton = new JRadioButton("Easy");
        mediumButton = new JRadioButton("Medium", true);
        hardButton = new JRadioButton("Hard");

        difficultyGroup.add(easyButton);
        difficultyGroup.add(mediumButton);
        difficultyGroup.add(hardButton);

        difficultyPanel.add(easyButton);
        difficultyPanel.add(mediumButton);
        difficultyPanel.add(hardButton);
        topPanel.add(difficultyPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        goFirstCheckBox = new JCheckBox("Go First");
        startButton = new JButton("Start Game");
        startButton.setEnabled(false);

        controlPanel.add(goFirstCheckBox);
        controlPanel.add(startButton);
        topPanel.add(controlPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
    }

    private void setupBoardPanel() {
        JPanel boardPanel = new JPanel(new BorderLayout());

        JLabel playerNameLabel = new JLabel(nameField.getText(), SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        boardPanel.add(playerNameLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(11, 11));
        gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gridPanel.add(new JLabel());
        for (int i = 1; i <= 10; i++) {
            JLabel colLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            colLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            colLabel.setPreferredSize(new Dimension(50, 50));
            gridPanel.add(colLabel);
        }

        playerCells = new JButton[10][10];
        for (int row = 0; row < 10; row++) {
            JLabel rowLabel = new JLabel(String.valueOf((char) ('A' + row)), SwingConstants.CENTER);
            rowLabel.setPreferredSize(new Dimension(50, 50));
            rowLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            gridPanel.add(rowLabel);
            for (int col = 0; col < 10; col++) {
                JButton cell = new JButton();
                cell.setBackground(new Color(0, 0, 139));
                cell.setPreferredSize(new Dimension(50, 50));
                cell.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1));
                playerCells[row][col] = cell;

                int finalRow = row;
                int finalCol = col;

                cell.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        if (draggedShip != null && SwingUtilities.isLeftMouseButton(e)) {
                            placeShipOnBoard(finalRow, finalCol);
                            draggedShip = null;
                            revalidate();
                            repaint();
                        }
                    }

                    public void mouseExited(MouseEvent e) {
                        //clearPreview();
                    }

                });

                cell.addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseMoved(MouseEvent e) {
                        if (draggedShip != null) {
                            showShipPreview(finalRow, finalCol);
                        }
                    }
                });

                cell.addMouseWheelListener(e -> {
                    if (draggedShip != null) {
                        alignment = alignment.orthogonal();
                        clearPreview();
                        showShipPreview(finalRow, finalCol);
                    }
                });
                gridPanel.add(cell);
            }
        }
        boardPanel.add(gridPanel, BorderLayout.CENTER);
        add(boardPanel, BorderLayout.CENTER);
    }


    private void setupShipPanel() {
        shipPanel = new JPanel();
        shipPanel.setLayout(new GridLayout(1, ShipClass.values().length, 5, 5));

        shipPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        shipPanel.setPreferredSize(new Dimension(300, 200));
        shipPanel.setMaximumSize(new Dimension(300, 200));
        shipPanel.setMinimumSize(new Dimension(300, 200));

        for (ShipClass shipClass : ShipClass.values()) {
            ImageIcon icon = new ImageIcon("resources/ships/" + shipClass.name() + ".png");
            Image image;
            if (shipClass.length() == 5) {
                image = icon.getImage().getScaledInstance(60, shipClass.length() * 30, Image.SCALE_SMOOTH);
            } else {
                image = icon.getImage().getScaledInstance(30, shipClass.length() * 30, Image.SCALE_SMOOTH);
            }
            JLabel shipLabel = new JLabel(new ImageIcon(image));

            shipLabel.setName(shipClass.name());

            shipLabels.add(shipLabel);
            shipOriginalImages.put(shipLabel, image);

            shipLabel.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    for (JLabel label : shipLabels) {
                        label.setIcon(new ImageIcon(shipOriginalImages.get(label)));
                    }

                    draggedShip = shipLabel;
                    draggedShipSize = shipClass.length();
                    draggedShipImage = createTransparentImage(shipOriginalImages.get(shipLabel));
                    shipLabel.setIcon(new ImageIcon(draggedShipImage));

                    checkStartButton();
                }
            });

            shipPanel.add(shipLabel);
        }

        add(shipPanel, BorderLayout.SOUTH);
    }

    private void setupComputerBoardPanel() {
        JPanel boardPanel = new JPanel(new BorderLayout());

        JLabel computerNameLabel = new JLabel("Computer", SwingConstants.CENTER);
        computerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        boardPanel.add(computerNameLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(11, 11));
        gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gridPanel.add(new JLabel());
        for (int i = 1; i <= 10; i++) {
            JLabel colLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            colLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            gridPanel.add(colLabel);
        }

        computerCells = new JButton[10][10];
        for (int row = 0; row < 10; row++) {
            JLabel rowLabel = new JLabel(String.valueOf((char) ('A' + row)), SwingConstants.CENTER);
            rowLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            gridPanel.add(rowLabel);
            for (int col = 0; col < 10; col++) {
                JButton cell = new JButton();
                cell.setBackground(new Color(0, 0, 139));
                cell.setEnabled(true);
                cell.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1));
                computerCells[row][col] = cell;
                gridPanel.add(cell);
            }
        }
        boardPanel.add(gridPanel, BorderLayout.CENTER);
        computerBoardPanel = boardPanel;
        computerBoardPanel.setVisible(false);
        add(computerBoardPanel, BorderLayout.EAST);
    }

    private void setupEventListeners() {
        nameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkStartButton();
            }
        });
        goFirstCheckBox.addActionListener(e -> checkStartButton());
        startButton.addActionListener(e -> startGame());

        computerBoardPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (mediumButton.isSelected() && playerTurn) {
                    JButton clickedCell = (JButton) e.getSource();
                    Coordinate target = getCellCoordinates(clickedCell);

                    if (target != null && !playerClickedCells[target.getX()][target.getY()]) {
                        playerClickedCells[target.getX()][target.getY()] = true;

                        if (usedCells[target.getX()][target.getY()]) {
                            clickedCell.setIcon(new ImageIcon("resources/effects/explosion.png"));
                            computerHits++;
                        } else {
                            clickedCell.setIcon(new ImageIcon("resources/effects/miss.png"));
                        }
                        clickedCell.setEnabled(false);
                        checkWinner();
                        playerTurn = false;
                        performComputerMove();
                    }
                }
            }
        });
    }

    private Coordinate getCellCoordinates(JButton cell) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (computerCells[row][col] == cell) {
                    return Coordinate.of(row, col);
                }
            }
        }
        return null;
    }

    private void startGame() {
        playerShipHits.clear();
        for (ShipClass shipClass : ShipClass.values()) {
            playerShipHits.put(shipClass, 0);
        }

        setSize(1200, 750);
        randomizeComputerShips();
        collectAllCoordinates();
        if (goFirstCheckBox.isSelected()) {
            playerTurn = false;
            performComputerMove();
        }
        getContentPane().removeAll();

        int cellSize = 50;


        JPanel playerBoardPanel = new JPanel(new BorderLayout());
        JLabel playerNameLabel = new JLabel(nameField.getText(), SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerBoardPanel.add(playerNameLabel, BorderLayout.NORTH);

        JPanel playerGridPanel = new JPanel(new GridLayout(11, 11));
        playerGridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        playerGridPanel.add(new JLabel(""));
        for (int i = 1; i <= 10; i++) {
            JLabel colLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            colLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            playerGridPanel.add(colLabel);
        }
        for (int row = 0; row < 10; row++) {
            JLabel rowLabel = new JLabel(String.valueOf((char) ('A' + row)), SwingConstants.CENTER);
            rowLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            playerGridPanel.add(rowLabel);
            for (int col = 0; col < 10; col++) {
                playerCells[row][col].setPreferredSize(new Dimension(cellSize, cellSize));
                playerCells[row][col].setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1));
                playerGridPanel.add(playerCells[row][col]);
            }
        }
        playerBoardPanel.add(playerGridPanel, BorderLayout.CENTER);


        JPanel computerBoardPanel = new JPanel(new BorderLayout());
        JLabel computerNameLabel = new JLabel("Computer", SwingConstants.CENTER);
        computerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        computerBoardPanel.add(computerNameLabel, BorderLayout.NORTH);

        ImageIcon smokeIcon = new ImageIcon("resources/effects/smoke.png");
        Image smokeImage = smokeIcon.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
        ImageIcon missIcon = new ImageIcon("resources/effects/miss.png");
        Image missImage = missIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon explosionIcon = new ImageIcon("resources/effects/explosion.png");
        Image explosionImage = explosionIcon.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);

        JPanel computerGridPanel = new JPanel(new GridLayout(11, 11));
        computerGridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        computerGridPanel.add(new JLabel(""));
        for (int i = 1; i <= 10; i++) {
            JLabel colLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            colLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            computerGridPanel.add(colLabel);
        }
        for (int row = 0; row < 10; row++) {
            JLabel rowLabel = new JLabel(String.valueOf((char) ('A' + row)), SwingConstants.CENTER);
            rowLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            computerGridPanel.add(rowLabel);
            for (int col = 0; col < 10; col++) {
                JButton cell = computerCells[row][col];
                cell.setPreferredSize(new Dimension(cellSize, cellSize));
                cell.setIcon(new ImageIcon(smokeImage));
                cell.setEnabled(true);
                cell.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1));

                int finalRow = row;
                int finalCol = col;

                cell.addActionListener(e -> {
                    if (!playerClickedCells[finalRow][finalCol]) {
                        playerClickedCells[finalRow][finalCol] = true;

                        if (computerShipCells[finalRow][finalCol]) {
                            cell.setIcon(new ImageIcon(explosionImage));
                            playerHits++;
                            checkAndRemoveDestroyedShip(false, finalRow, finalCol);
                            checkWinner();
                        } else {
                            cell.setIcon(new ImageIcon(missImage));
                        }

                        if (playerTurn) {
                            playerTurn = false;
                            performComputerMove();
                        }
                    }
                });

                computerGridPanel.add(cell);
            }
        }
        computerBoardPanel.add(computerGridPanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, playerBoardPanel, computerBoardPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(5);

        winnerLabel = new JLabel("                       Your Move!", SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(restartButton);
        controlPanel.add(exitButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(winnerLabel, BorderLayout.CENTER);
        topPanel.add(controlPanel, BorderLayout.EAST);

        playerShipsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        for (ShipClass shipClass : ShipClass.values()) {
            ImageIcon icon = new ImageIcon("resources/ships/" + shipClass.name() + ".png");
            Image image;
            if (shipClass.length() == 5) {
                image = icon.getImage().getScaledInstance(60, shipClass.length() * 30, Image.SCALE_SMOOTH);
            } else {
                image = icon.getImage().getScaledInstance(30, shipClass.length() * 30, Image.SCALE_SMOOTH);
            }
            JLabel shipLabel = new JLabel(new ImageIcon(image));
            shipLabel.setName(shipClass.name());
            playerShipLabels.add(shipLabel);
            playerShipsPanel.add(shipLabel);
        }

        computerShipsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        for (ShipClass shipClass : ShipClass.values()) {
            ImageIcon icon = new ImageIcon("resources/ships/" + shipClass.name() + ".png");
            Image image;
            if (shipClass.length() == 5) {
                image = icon.getImage().getScaledInstance(60, shipClass.length() * 30, Image.SCALE_SMOOTH);
            } else {
                image = icon.getImage().getScaledInstance(30, shipClass.length() * 30, Image.SCALE_SMOOTH);
            }
            JLabel shipLabel = new JLabel(new ImageIcon(image));
            computerShipLabels.add(shipLabel);
            computerShipsPanel.add(shipLabel);
        }

        playerShipsPanel.revalidate();
        playerShipsPanel.repaint();
        computerShipsPanel.revalidate();
        computerShipsPanel.repaint();

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.add(playerShipsPanel);
        bottomPanel.add(computerShipsPanel);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void restartGame() {
        dispose();
        new BattleshipGUI();
    }

    private void checkWinner() {
        if (playerHits == totalShipFields) {
            winnerLabel.setText("                       Winner: " + nameField.getText());
            preventFurtherMoves();
            clearSmokeFromComputerBoard();
        } else if (computerHits == totalShipFields) {
            winnerLabel.setText("                       Winner: Computer");
            preventFurtherMoves();
            revealComputerShips();
            clearSmokeFromComputerBoard();
        }

        checkAndRemoveDestroyedShip(true);
        checkAndRemoveDestroyedShip(false);
    }

    private void clearSmokeFromComputerBoard() {
        for (int row = 0; row < computerCells.length; row++) {
            for (int col = 0; col < computerCells[row].length; col++) {

                if (!playerClickedCells[row][col]) {
                    computerCells[row][col].setIcon(null);
                }
            }
        }
    }
    private void revealComputerShips() {
        for (Map.Entry<ShipClass, List<Coordinate>> entry : computerShip.entrySet()) {
            ShipClass shipClass = entry.getKey();
            List<Coordinate> ship = entry.getValue();

            boolean isShipDestroyed = isShipDestroyed(ship);

            ImageIcon shipIcon = new ImageIcon("resources/ships/" + shipClass.name() + ".png");
            Image shipImage = shipIcon.getImage();

            boolean isVertical = isShipVertical(ship);

            if (!isVertical) {
                shipImage = rotateImage(toBufferedImage(shipImage));
            }

            BufferedImage[] shipParts = splitShipImage(toBufferedImage(shipImage), ship.size(), isVertical);

            for (int i = 0; i < ship.size(); i++) {
                Coordinate part = ship.get(i);
                JButton cell = computerCells[part.getX()][part.getY()];

                JLayeredPane layeredPane = new JLayeredPane();
                layeredPane.setPreferredSize(new Dimension(50, 50));

                JLabel shipLabel = new JLabel(new ImageIcon(shipParts[i].getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                shipLabel.setBounds(0, 0, 50, 50);
                layeredPane.add(shipLabel, JLayeredPane.DEFAULT_LAYER);

                if (playerClickedCells[part.getX()][part.getY()]) {
                    ImageIcon explosionIcon = new ImageIcon("resources/effects/explosion.png");
                    JLabel explosionLabel = new JLabel(new ImageIcon(explosionIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                    explosionLabel.setBounds(0, 0, 50, 50);
                    layeredPane.add(explosionLabel, JLayeredPane.PALETTE_LAYER);
                }

                if (isShipDestroyed) {
                    ImageIcon explosionIcon = new ImageIcon("resources/effects/explosion.png");
                    JLabel explosionLabel = new JLabel(new ImageIcon(explosionIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                    explosionLabel.setBounds(0, 0, 50, 50);
                    layeredPane.add(explosionLabel, JLayeredPane.PALETTE_LAYER);
                }

                cell.setLayout(new BorderLayout());
                cell.removeAll();
                cell.add(layeredPane, BorderLayout.CENTER);
                cell.revalidate();
                cell.repaint();
            }
        }
    }

    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }

    private boolean isShipVertical(List<Coordinate> ship) {
        int col = ship.get(0).getY();
        for (Coordinate part : ship) {
            if (part.getY() != col) {
                return false;
            }
        }
        return true;
    }

    private BufferedImage[] splitShipImage(BufferedImage shipImage, int parts, boolean isVertical) {
        BufferedImage[] shipParts = new BufferedImage[parts];
        int width = shipImage.getWidth();
        int height = shipImage.getHeight();

        if (isVertical) {
            int partHeight = height / parts;
            for (int i = 0; i < parts; i++) {
                shipParts[i] = new BufferedImage(width, partHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = shipParts[i].createGraphics();
                g2d.drawImage(shipImage, 0, -i * partHeight, null);
                g2d.dispose();
            }
        } else {
            int partWidth = width / parts;
            for (int i = 0; i < parts; i++) {
                shipParts[i] = new BufferedImage(partWidth, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = shipParts[i].createGraphics();
                g2d.drawImage(shipImage, -i * partWidth, 0, null);
                g2d.dispose();
            }
        }

        return shipParts;
    }


    private void checkAndRemoveDestroyedShip(boolean isPlayer) {
        List<Map.Entry<ShipClass, List<Coordinate>>> ships = isPlayer ? playerShips : computerShips;
        List<JLabel> labels = isPlayer ? playerShipLabels : computerShipLabels;

        for (Map.Entry<ShipClass, List<Coordinate>> entry : ships) {
            ShipClass shipClass = entry.getKey();
            List<Coordinate> ship = entry.getValue();
            boolean destroyed = isPlayer ? isPlayerShipDestroyed(ship) : isComputerShipDestroyed(ship);

            if (destroyed && destroyedShips.contains(shipClass)) {
                for (JLabel label : labels) {
                    if (label.getName().equals(shipClass.name())) {
                        dimShipLabel(label);
                        destroyedShips.add(shipClass);
                        break;
                    }
                }
            }
        }
    }

    private void dimShipLabel(JLabel shipLabel) {
        ImageIcon icon = (ImageIcon) shipLabel.getIcon();
        if (icon != null) {
            Image image = icon.getImage();
            Image dimmedImage = createTransparentImage(image);
            shipLabel.setIcon(new ImageIcon(dimmedImage));
        }
        playerShipsPanel.revalidate();
        playerShipsPanel.repaint();
    }

    private void checkAndRemoveDestroyedShip(boolean isPlayer, int hitRow, int hitCol) {
        List<Map.Entry<ShipClass, List<Coordinate>>> ships = isPlayer ? playerShips : computerShips;
        List<JLabel> labels = isPlayer ? playerShipLabels : computerShipLabels;

        for (int i = 0; i < ships.size(); i++) {
            Map.Entry<ShipClass, List<Coordinate>> shipEntry = ships.get(i);
            List<Coordinate> ship = shipEntry.getValue();
            if (ship.contains(Coordinate.of(hitRow, hitCol))) {
                boolean destroyed = isPlayer ? isPlayerShipDestroyed(ship) : isComputerShipDestroyed(ship);
                if (destroyed) {
                    dimShipLabel(labels.get(i));
                    ships.remove(i);
                    labels.remove(i);
                    if (!isPlayer) {
                        revealDestroyedShip(ship);
                        removeExplosionImages(ship);
                    }
                    break;
                }
            }
        }
    }

    private boolean isPlayerShipDestroyed(List<Coordinate> ship) {
        for (Coordinate part : ship) {
            if (!computerClickedCells[part.getX()][part.getY()]) {
                return false;
            }
        }
        return true;
    }

    private boolean isComputerShipDestroyed(List<Coordinate> ship) {
        for (Coordinate part : ship) {

            if (!playerClickedCells[part.getX()][part.getY()]) {
                return false;
            }
        }
        return true;
    }

    private void removeExplosionImages(List<Coordinate> ship) {
        for (Coordinate part : ship) {
            JButton cell = computerCells[part.getX()][part.getY()];
            cell.setIcon(null);
            cell.revalidate();
            cell.repaint();
        }
    }

    private void revealDestroyedShip(List<Coordinate> ship) {
        for (Map.Entry<ShipClass, List<Coordinate>> entry : computerShip.entrySet()) {
            if (entry.getValue().equals(ship)) {
                ShipClass shipClass = entry.getKey();
                for (Coordinate part : ship) {
                    JButton cell = computerCells[part.getX()][part.getY()];
                    JLayeredPane layeredPane = new JLayeredPane();
                    layeredPane.setPreferredSize(new Dimension(50, 50));

                    ImageIcon shipIcon = new ImageIcon("resources/ships/" + shipClass.name() + ".png");
                    Image shipImage = shipIcon.getImage();
                    if (!isShipVertical(ship)) {
                        shipImage = rotateImage(toBufferedImage(shipImage));
                    }
                    BufferedImage[] shipParts = splitShipImage(toBufferedImage(shipImage), ship.size(), isShipVertical(ship));
                    JLabel shipLabel = new JLabel(new ImageIcon(shipParts[ship.indexOf(part)].getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                    shipLabel.setBounds(0, 0, 50, 50);
                    layeredPane.add(shipLabel, JLayeredPane.DEFAULT_LAYER);

                    ImageIcon explosionIcon = new ImageIcon("resources/effects/explosion.png");
                    JLabel explosionLabel = new JLabel(new ImageIcon(explosionIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                    explosionLabel.setBounds(0, 0, 50, 50);
                    layeredPane.add(explosionLabel, JLayeredPane.PALETTE_LAYER);

                    cell.setLayout(new BorderLayout());
                    cell.removeAll();
                    cell.add(layeredPane, BorderLayout.CENTER);
                    cell.revalidate();
                    cell.repaint();
                }
                break;
            }
        }
    }

    private boolean isShipDestroyed(List<Coordinate> ship) {
        for (Coordinate part : ship) {
            if (!playerClickedCells[part.getX()][part.getY()]) {
                return false;
            }
        }
        return true;
    }

    private void preventFurtherMoves() {
        for (JButton[] row : computerCells) {
            for (JButton cell : row) {
                for (ActionListener al : cell.getActionListeners()) {
                    cell.removeActionListener(al);
                }
            }
        }
    }

    private void collectAllCoordinates() {
        allCoordinates.clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                allCoordinates.add(Coordinate.of(row, col));
            }
        }

        Collections.shuffle(allCoordinates, random);
    }


    private void performComputerMove() {
        if (playerHits == totalShipFields) {
            return;
        }
        Coordinate target = null;
        if (easyButton.isSelected()) {
            target = getRandomTarget(getAvailableTargets());
        } else if (mediumButton.isSelected()) {
            List<Coordinate> damaged = getDamagedCells();
            Set<Coordinate> availableTargets = getAvailableTargets();

            if (!damaged.isEmpty()) {
                Set<Coordinate> targets = new HashSet<>();
                for (Coordinate hit : damaged) {
                    for (Coordinate neighbor : getNeighbors(hit, availableTargets)) {
                        if (availableTargets.contains(neighbor)) {
                            targets.add(neighbor);
                        }
                    }
                }

                if (!targets.isEmpty()) {
                    if (damaged.size() == 1) {
                        target = targets.iterator().next();
                    } else {
                        Coordinate last = damaged.get(damaged.size() - 1);
                        Coordinate secondLast = damaged.get(damaged.size() - 2);
                        Optional<Alignment> alignmentOpt = secondLast.computeAlignment(last);

                        if (alignmentOpt.isPresent()) {
                            Alignment alignment = alignmentOpt.get();
                            List<Coordinate> alignedTargets = targets.stream()
                                    .filter(candidate -> {
                                        if (alignment == Alignment.HORIZONTAL) {
                                            return candidate.getY() == last.getY();
                                        } else {
                                            return candidate.getX() == last.getX();
                                        }
                                    }).toList();
                            if (!alignedTargets.isEmpty()) {
                                target = alignedTargets.get(0);
                            }
                        }

                        if (target == null) {
                            target = targets.iterator().next();
                        }
                    }
                }
            }

            if (target == null) {
                target = getRandomTarget(availableTargets);
            }
        } else if (hardButton.isSelected()) {

            List<Coordinate> damaged = getDamagedCells();
            Set<Coordinate> availableTargets = getAvailableTargets();

            if (!damaged.isEmpty()) {
                target = huntStrategyNext(damaged, availableTargets);
            } else {
                target = probabilityStrategyNext(availableTargets);
            }

        }
        if (target != null) {
            attackCell(target);
        }
        playerTurn = true;
    }

    private Coordinate huntStrategyNext(List<Coordinate> damaged, Set<Coordinate> availableTargets) {
        Set<Coordinate> targets = new HashSet<>();
        for (Coordinate hit : damaged) {
            for (Coordinate neighbor : getNeighbors(hit, availableTargets)) {
                if (availableTargets.contains(neighbor)) {
                    targets.add(neighbor);
                }
            }
        }

        if (!targets.isEmpty()) {
            if (damaged.size() != 1) {
                Coordinate last = damaged.get(damaged.size() - 1);
                Coordinate secondLast = damaged.get(damaged.size() - 2);
                Optional<Alignment> alignmentOpt = secondLast.computeAlignment(last);

                if (alignmentOpt.isPresent()) {
                    Alignment alignment = alignmentOpt.get();
                    List<Coordinate> alignedTargets = targets.stream()
                            .filter(candidate -> {
                                if (alignment == Alignment.HORIZONTAL) {
                                    return candidate.getY() == last.getY();
                                } else {
                                    return candidate.getX() == last.getX();
                                }
                            }).toList();
                    if (!alignedTargets.isEmpty()) {
                        return alignedTargets.get(0);
                    }
                }

            }
            return targets.iterator().next();
        }

        return getRandomTarget(availableTargets);
    }

    private Coordinate probabilityStrategyNext(Set<Coordinate> availableTargets) {
        if (availableTargets.isEmpty()) {
            throw new IllegalStateException();
        }

        Map<Coordinate, Integer> scoreMap = new HashMap<>();
        for (Coordinate c : availableTargets) {
            scoreMap.put(c, 0);
        }

        for (ShipClass shipClass : remainingShipClasses()) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    Coordinate ref = Coordinate.of(x, y);
                    for (Alignment alignment : new Alignment[]{Alignment.HORIZONTAL, Alignment.VERTICAL}) {
                        Set<Coordinate> offsets = shipClass.points(alignment);
                        Set<Coordinate> placement = new HashSet<>();
                        boolean validPlacement = true;
                        for (Coordinate offset : offsets) {
                            Coordinate pos = ref.add(offset);
                            if (pos.getX() < 0 || pos.getX() >= 10 || pos.getY() < 0 || pos.getY() >= 10) {
                                validPlacement = false;
                                break;
                            }
                            if (!availableTargets.contains(pos)) {
                                validPlacement = false;
                                break;
                            }
                            placement.add(pos);
                        }
                        if (validPlacement) {
                            for (Coordinate pos : placement) {
                                scoreMap.put(pos, scoreMap.get(pos) + 1);
                            }
                        }
                    }
                }
            }
        }

        Coordinate bestTarget = null;
        int maxScore = -1;
        for (Map.Entry<Coordinate, Integer> entry : scoreMap.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                bestTarget = entry.getKey();
            }
        }

        if (bestTarget == null) {
            bestTarget = availableTargets.iterator().next();
        }
        return bestTarget;
    }
    private List<Coordinate> getDamagedCells() {
        List<Coordinate> damaged = new ArrayList<>();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (usedCells[row][col] && computerClickedCells[row][col]) {
                    damaged.add(Coordinate.of(row, col));
                }
            }
        }
        return damaged;
    }

    private Set<Coordinate> getAvailableTargets() {
        Set<Coordinate> available = new HashSet<>();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (!computerClickedCells[row][col]) {
                    available.add(Coordinate.of(row, col));
                }
            }
        }
        return available;
    }

    private Set<Coordinate> getNeighbors(Coordinate coord, Set<Coordinate> availableTargets) {
        Set<Coordinate> neighbors = new HashSet<>();
        int x = coord.getX();
        int y = coord.getY();

        if (x > 0 && availableTargets.contains(Coordinate.of(x - 1, y))) {
            neighbors.add(Coordinate.of(x - 1, y));
        }
        if (x < 9 && availableTargets.contains(Coordinate.of(x + 1, y))) {
            neighbors.add(Coordinate.of(x + 1, y));
        }
        if (y > 0 && availableTargets.contains(Coordinate.of(x, y - 1))) {
            neighbors.add(Coordinate.of(x, y - 1));
        }
        if (y < 9 && availableTargets.contains(Coordinate.of(x, y + 1))) {
            neighbors.add(Coordinate.of(x, y + 1));
        }

        return neighbors;
    }

    private  Set<ShipClass> remainingShipClasses() {
        Set<ShipClass> remaining = new HashSet<>();
        for (ShipClass shipClass : ShipClass.values()) {
            if (playerShipHits.get(shipClass) < shipClass.length()) {
                remaining.add(shipClass);
            }
        }
        return remaining;
    }
    private Coordinate getRandomTarget(Set<Coordinate> availableTargets) {
        if (availableTargets.isEmpty()) {
            return null;
        }
        int index = random.nextInt(availableTargets.size());
        return (Coordinate) availableTargets.toArray()[index];
    }

    private void updateShipState(int row, int col) {
        for (Map.Entry<ShipClass, List<Coordinate>> entry : playerShips) {
            ShipClass shipClass = entry.getKey();
            List<Coordinate> ship = entry.getValue();
            if (ship.contains(Coordinate.of(row, col))) {
                playerShipHits.put(shipClass, playerShipHits.get(shipClass) + 1);

                if (playerShipHits.get(shipClass) == shipClass.length()) {
                    dimPlayerShipLabel(shipClass);
                }
                break;
            }
        }
    }

    private void attackCell(Coordinate target) {
        JButton playerCell = playerCells[target.getX()][target.getY()];
        ImageIcon missIcon = new ImageIcon("resources/effects/miss.png");
        ImageIcon explosionIcon = new ImageIcon("resources/effects/explosion.png");

        if (usedCells[target.getX()][target.getY()]) {
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(50, 50));

            if (playerCell.getIcon() != null) {
                JLabel shipLabel = new JLabel(playerCell.getIcon());
                shipLabel.setBounds(0, 0, 50, 50);
                layeredPane.add(shipLabel, JLayeredPane.DEFAULT_LAYER);
            }

            JLabel explosionLabel = new JLabel(new ImageIcon(explosionIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            explosionLabel.setBounds(0, 0, 50, 50);
            layeredPane.add(explosionLabel, JLayeredPane.PALETTE_LAYER);

            playerCell.setLayout(new BorderLayout());
            playerCell.removeAll();
            playerCell.add(layeredPane, BorderLayout.CENTER);
            playerCell.revalidate();
            playerCell.repaint();

            computerHits++;

            updateShipState(target.getX(), target.getY());
            checkAndRemoveDestroyedShip(true, target.getX(), target.getY());
            checkWinner();
        } else {
            playerCell.setIcon(new ImageIcon(missIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        }

        computerClickedCells[target.getX()][target.getY()] = true;
        playerCell.setDisabledIcon(playerCell.getIcon());
        playerCell.setEnabled(true);
        playerCell.repaint();
    }

    private void dimPlayerShipLabel(ShipClass shipClass) {
        for (JLabel label : playerShipLabels) {
            if (label.getName() != null && label.getName().equals(shipClass.name())) {
                ImageIcon icon = (ImageIcon) label.getIcon();
                if (icon != null) {
                    Image image = icon.getImage();
                    Image dimmedImage = createTransparentImage(image);
                    label.setIcon(new ImageIcon(dimmedImage));
                }
            }
        }
        playerShipsPanel.revalidate();
        playerShipsPanel.repaint();
    }

    private void randomizeComputerShips() {
        Random random = new Random();
        computerShipHits.clear();
        for (ShipClass shipClass : ShipClass.values()) {
            computerShipHits.put(shipClass, 0);
        }

        for (ShipClass shipClass : ShipClass.values()) {
            boolean placed = false;

            while (!placed) {
                int row = random.nextInt(10);
                int col = random.nextInt(10);
                boolean vertical = random.nextBoolean();

                if (canPlaceShip(row, col, shipClass.length(), vertical, computerCells)) {
                    placeShip(row, col, shipClass.length(), vertical, computerCells);
                    placed = true;

                    List<Coordinate> shipPoints = new ArrayList<>();
                    if (vertical) {
                        for (int j = 0; j < shipClass.length(); j++) {
                            computerShipCells[row + j][col] = true;
                            shipPoints.add(Coordinate.of(row + j, col));
                        }
                    } else {
                        for (int j = 0; j < shipClass.length(); j++) {
                            computerShipCells[row][col + j] = true;
                            shipPoints.add(Coordinate.of(row, col + j));
                        }
                    }

                    Map.Entry<ShipClass, List<Coordinate>> shipEntry =
                            new AbstractMap.SimpleEntry<>(shipClass, shipPoints);
                    computerShips.add(shipEntry);

                    computerShip.put(shipClass, shipPoints);

                    ImageIcon shipIcon = new ImageIcon("resources/ships/" + shipClass.name() + ".png");
                    Image shipImage = shipIcon.getImage();
                    if (!vertical) {
                        shipImage = rotateImage(toBufferedImage(shipImage));
                    }
                    BufferedImage[] shipParts = splitShipImage(toBufferedImage(shipImage), shipClass.length(), vertical);

                    for (int i = 0; i < shipClass.length(); i++) {
                        Coordinate part = shipPoints.get(i);
                        JButton cell = computerCells[part.getX()][part.getY()];
                        cell.setIcon(new ImageIcon(shipParts[i].getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                        cell.setEnabled(false);
                    }
                }
            }
        }
    }


    private boolean canPlaceShip(int row, int col, int size, boolean vertical, JButton[][] board) {
        if (vertical) {
            if (row + size > 10) return false;
            for (int i = 0; i < size; i++) {
                if (!board[row + i][col].isEnabled()) return false;
            }
        } else {
            if (col + size > 10) return false;
            for (int i = 0; i < size; i++) {
                if (!board[row][col + i].isEnabled()) return false;
            }
        }
        return true;
    }

    private void placeShip(int row, int col, int size, boolean vertical, JButton[][] board) {
        for (int i = 0; i < size; i++) {
            if (vertical) {
                board[row + i][col].setEnabled(false);
            } else {
                board[row][col + i].setEnabled(false);
            }
        }
    }

    private void showShipPreview(int row, int col) {
        clearPreview();
        if (draggedShip == null) return;

        BufferedImage originalImage = toBufferedImage(shipOriginalImages.get(draggedShip));

        if (alignment == Alignment.VERTICAL) {
            int offset = draggedShipSize / 2;
            int startRow = row - offset;
            if (startRow < 0) startRow = 0;
            if (startRow + draggedShipSize > 10) startRow = 10 - draggedShipSize;

            for (int i = 0; i < draggedShipSize; i++) {
                if (usedCells[startRow + i][col]) return;
            }

            for (int i = 0; i < draggedShipSize; i++) {
                BufferedImage part = originalImage.getSubimage(0, i * 30, 30, 30);
                Image image = part.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                playerCells[startRow + i][col].setIcon(new ImageIcon(image));
            }
        } else {
            int offset = draggedShipSize / 2;
            int startCol = col - offset;
            if (startCol < 0) startCol = 0;
            if (startCol + draggedShipSize > 10) startCol = 10 - draggedShipSize;

            for (int i = 0; i < draggedShipSize; i++) {
                if (usedCells[row][startCol + i]) return;
            }

            BufferedImage rotatedImage = rotateImage(originalImage);
            for (int i = 0; i < draggedShipSize; i++) {
                BufferedImage part = rotatedImage.getSubimage(i * 30, 0, 30, 30);
                Image image = part.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                playerCells[row][startCol + i].setIcon(new ImageIcon(image));
            }
        }
    }


    private void clearPreview() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (playerCells[row][col].isEnabled() && !usedCells[row][col]) {
                    playerCells[row][col].setIcon(null);
                }
            }
        }
    }

    private void placeShipOnBoard(int row, int col) {
        if (draggedShip == null) return;
        clearPreview();
        String shipClassName = draggedShip.getName();
        if (shipClassName == null) {
            return;
        }

        ShipClass shipClass;
        try {
            shipClass = ShipClass.valueOf(shipClassName);
        } catch (IllegalArgumentException e) {
            return;
        }

        if (alignment == Alignment.VERTICAL) {
            int offset = shipClass.length() / 2;
            int startRow = row - offset;
            if (startRow < 0) startRow = 0;
            if (startRow + shipClass.length() > 10) startRow = 10 - shipClass.length();

            for (int i = 0; i < shipClass.length(); i++) {
                if (usedCells[startRow + i][col]) {
                    draggedShip.setIcon(new ImageIcon(shipOriginalImages.get(draggedShip)));
                    return;
                }
            }

            BufferedImage shipImage = toBufferedImage(shipOriginalImages.get(draggedShip));
            BufferedImage[] shipParts = splitShipImage(shipImage, shipClass.length(), true);

            List<Coordinate> shipCoordinates = new ArrayList<>();
            for (int i = 0; i < shipClass.length(); i++) {
                ImageIcon icon = new ImageIcon(shipParts[i].getScaledInstance(55, 45, Image.SCALE_SMOOTH));
                playerCells[startRow + i][col].setIcon(icon);
                playerCells[startRow + i][col].setEnabled(false);
                usedCells[startRow + i][col] = true;
                shipCoordinates.add(Coordinate.of(startRow + i, col));
            }
            playerShips.add(new AbstractMap.SimpleEntry<>(shipClass, shipCoordinates));
        } else {
            int offset = shipClass.length() / 2;
            int startCol = col - offset;
            if (startCol < 0) startCol = 0;
            if (startCol + shipClass.length() > 10) startCol = 10 - shipClass.length();

            for (int i = 0; i < shipClass.length(); i++) {
                if (usedCells[row][startCol + i]) {
                    draggedShip.setIcon(new ImageIcon(shipOriginalImages.get(draggedShip)));
                    return;
                }
            }

            BufferedImage shipImage = toBufferedImage(shipOriginalImages.get(draggedShip));
            BufferedImage rotatedImage = rotateImage(shipImage);
            BufferedImage[] shipParts = splitShipImage(rotatedImage, shipClass.length(), false);

            List<Coordinate> shipCoordinates = new ArrayList<>();
            for (int i = 0; i < shipClass.length(); i++) {
                ImageIcon icon = new ImageIcon(shipParts[i].getScaledInstance(55, 50, Image.SCALE_SMOOTH));
                playerCells[row][startCol + i].setIcon(icon);
                playerCells[row][startCol + i].setEnabled(false);
                usedCells[row][startCol + i] = true;
                shipCoordinates.add(Coordinate.of(row, startCol + i));
            }
            playerShips.add(new AbstractMap.SimpleEntry<>(shipClass, shipCoordinates));
        }
        shipsPlaced++;

        shipPanel.remove(draggedShip);
        shipPanel.revalidate();
        shipPanel.repaint();

        checkStartButton();
    }

    private BufferedImage rotateImage(BufferedImage image) {
        BufferedImage rotatedImage = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();

        g2d.rotate(Math.toRadians(90), image.getHeight() / 2.0, image.getHeight() / 2.0);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }

    private void checkStartButton() {
        String name = nameField.getText().trim();
        boolean nameEntered = !name.isEmpty() && !name.equals("Write your Name");
        boolean difficultySelected = easyButton.isSelected() || mediumButton.isSelected() || hardButton.isSelected();

        startButton.setEnabled(nameEntered && difficultySelected && shipsPlaced == totalShips);
    }

    private BufferedImage createTransparentImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage transparentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = transparentImage.createGraphics();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5));
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return transparentImage;
    }
}