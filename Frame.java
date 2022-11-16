import javax.lang.model.util.ElementScanner6;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;

public class Frame extends JFrame implements KeyListener, ActionListener {
    String passage = "", typedPass = "", message = "";
    int typed = 0, count = 0, wrong = 0, wpm, cpm;
    double start, end, elapsed, seconds, acc;
    boolean running, ended;
    final int SCREEN_WIDTH, SCREEN_HEIGHT, DELAY = 100;

    JButton button;
    Timer timer;
    JLabel label;

    public Frame() {
        super("Typing Test");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SCREEN_WIDTH = 720;
        SCREEN_HEIGHT = 400;
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setVisible(true);
        setLocationRelativeTo(null);
        button = new JButton("Start");
        button.setFont(new Font("Algerian", Font.BOLD, 30));
        button.setForeground(Color.BLUE);
        button.setVisible(true);
        button.addActionListener(this);
        button.setFocusable(false);
        label = new JLabel("Click the start button");
        label.setFont(new Font("Algerian", Font.BOLD, 30));
        label.setVisible(true);
        label.setOpaque(true);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.BLACK);
        label.setBackground(new Color(252,186,3));
        add(button, BorderLayout.SOUTH);
        add(label, BorderLayout.NORTH);
        getContentPane().setBackground(new Color(29,31,30));
        addKeyListener(this);
        setFocusable(true);
        setResizable(false);
        revalidate();
    }

    public static String getPassage() {
        String[] passages = {
                "a group of words, usually containing a verb, that expresses a thought in the form of a statement, question, instruction, or exclamation and starts with a capital letter when written Topic sentences are similar to mini thesis statements. Like a thesis statement, a topic sentence has a specific main point. Whereas the thesis is the main point of the essay, the topic sentence is the main point of the paragraph. Like the thesis statement, a topic sentence has a unifying function. But a thesis statement or topic sentence alone doesn't guarantee unity. An essay is unified if all the paragraphs relate to the thesis, whereas a paragraph is unified if all the sentences relate to the topic sentence. ",
                "a punishment given by a judge in court to a person or organization after they have been found guilty of doing something wrong A well-organized paragraph supports or develops a single controlling idea, which is expressed in a sentence called the topic sentence. A topic sentence has several important functions: it substantiates or supports an essay's thesis statement; it unifies the content of a paragraph and directs the order of the sentences; and it advises the reader of the subject to be discussed and how the paragraph will discuss it. Readers generally look to the first few sentences in a paragraph to determine the subject and perspective of the paragraph.",
                "a group of words, usually containing a subject and a verb, expressing a statement, question, instruction, or exclamation, and, when written, starting with a capital letter and ending with a period or other markTopic sentences are similar to mini thesis statements. Like a thesis statement, a topic sentence has a specific main point. Whereas the thesis is the main point of the essay, the topic sentence is the main point of the paragraph. Like the thesis statement, a topic sentence has a unifying function. But a thesis statement or topic sentence alone doesn't guarantee unity. An essay is unified if all the paragraphs relate to the thesis, whereas a paragraph is unified if all the sentences relate to the topic sentence. ",
                "a group of words, usually containing a subject and a verb, expressing a statement, question, instruction, or exclamation, and, when written, starting with a capital letter and ending with a period or other mark",
                "A sentence is a group of words which, when they are written down, begin with a capital letter and end with a full stop, question mark, or exclamation mark. Most sentences contain a subject and a verb Topic sentences are similar to mini thesis statements. Like a thesis statement, a topic sentence has a specific main point. Whereas the thesis is the main point of the essay, the topic sentence is the main point of the paragraph. Like the thesis statement, a topic sentence has a unifying function. But a thesis statement or topic sentence alone doesn't guarantee unity. An essay is unified if all the paragraphs relate to the thesis, whereas a paragraph is unified if all the sentences relate to the topic sentence. ",
                "A well-organized paragraph supports or develops a single controlling idea, which is expressed in a sentence called the topic sentence. A topic sentence has several important functions: it substantiates or supports an essay's thesis statement; it unifies the content of a paragraph and directs the order of the sentences; and it advises the reader of the subject to be discussed and how the paragraph will discuss it. Readers generally look to the first few sentences in a paragraph to determine the subject and perspective of the paragraph. That's why it's often best to put the topic sentence at the very beginning of the paragraph. In some cases, however, it's more effective to place another sentence before the topic sentence—for example, a sentence linking the current paragraph to the previous one, or one providing background information",
                "Although most paragraphs should have a topic sentence, there are a few situations when a paragraph might not need a topic sentence. For example, you might be able to omit a topic sentence in a paragraph that narrates a series of events, if a paragraph continues developing an idea that you introduced (with a topic sentence) in the previous paragraph, or if all the sentences and details in a paragraph clearly refer—perhaps indirectly—to a main point. The vast majority of your paragraphs, however, should have a topic sentence.",
                "In a coherent paragraph, each sentence relates clearly to the topic sentence or controlling idea, but there is more to coherence than this. If a paragraph is coherent, each sentence flows smoothly into the next without obvious shifts or jumps. A coherent paragraph also highlights the ties between old information and new information to make the structure of ideas or arguments clear to the reader.",
                "Along with the smooth flow of sentences, a paragraph's coherence may also be related to its length. If you have written a very long paragraph, one that fills a double-spaced typed page, for example, you should check it carefully to see if it should start a new paragraph where the original paragraph wanders from its controlling idea. On the other hand, if a paragraph is very short (only one or two sentences, perhaps), you may need to develop its controlling idea more thoroughly, or combine it with another paragraph.",
                "I don't wish to deny that the flattened, minuscule head of the large-bodied  houses little brain from our subjective, top-heavy perspective, BUT I do wish to assert that we should not expect more of the beast. FIRST OF ALL, large animals have relatively smaller brains than related, small animals. The correlation of brain size with body size among kindred animals (all reptiles, all mammals, FOR EXAMPLE) is remarkably regular. AS we move from small to large animals, from mice to elephants or small lizards to Komodo dragons, brain size increases, BUT not so fast as body size. IN OTHER WORDS, bodies grow faster than brains, AND large animals have low ratios of brain weight to body weight.",
                "Use transition words or phrases between sentences and between paragraphs. Transitional expressions emphasize the relationships between ideas, so they help readers follow your train of thought or see connections that they might otherwise miss or misunderstand. The following paragraph shows how carefully chosen transitions lead the reader smoothly from the introduction to the conclusion of the paragraph.",
                "Topic sentences are similar to mini thesis statements. Like a thesis statement, a topic sentence has a specific main point. Whereas the thesis is the main point of the essay, the topic sentence is the main point of the paragraph. Like the thesis statement, a topic sentence has a unifying function. But a thesis statement or topic sentence alone doesn't guarantee unity. An essay is unified if all the paragraphs relate to the thesis, whereas a paragraph is unified if all the sentences relate to the topic sentence. "
        };
        int place = (int) (Math.random() * passages.length);
        return passages[place].substring(0, 150).strip() + ".";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            passage = getPassage();
            timer = new Timer(DELAY, this);
            timer.start();
            running = true;
            ended = false;
            typedPass = message = "";
            wrong = typed = count = 0;
        }
        if (running) {
            repaint();
        } else if (ended) {
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (passage.length() > 1) {
            if (count == 0) {
                start = LocalTime.now().toNanoOfDay();
            } else if (typedPass.length() >= 150) {
                end = LocalTime.now().toNanoOfDay();
                elapsed = end - start;
                seconds = elapsed / 1000000000;
                wpm = (int) (((150.0 / 5) / seconds) * 60);
                cpm=wpm*5;
                acc = ((double)typed / count) * 100;
                ended = true;
                running = false;
            }
            char[] pass = passage.toCharArray();
            if (typedPass.length() < 150) {
                running = true;
                if (e.getKeyChar() == pass[typed]) {
                    typedPass += pass[typed++];
                    wrong = typed;
                } 
                count++;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setFont(new Font("Cascadia Code", Font.BOLD, 25));
        if (running) {
            g.setColor(new Color(150, 158, 153));
            if (passage.length() > 1) {
                g.drawString(passage.substring(0, 50), g.getFont().getSize()+20, g.getFont().getSize() * 6);
                g.drawString(passage.substring(50, 100), g.getFont().getSize()+20, g.getFont().getSize() * 8);
                g.drawString(passage.substring(100, 150), g.getFont().getSize()+20, g.getFont().getSize() * 10);
            }
            g.setColor(Color.GREEN);
            if (typedPass.length() > 0) {
                if(typed<50)
                    g.drawString(typedPass.substring(0, typed), g.getFont().getSize()+20, g.getFont().getSize() * 6);
                else
                    g.drawString(typedPass.substring(0, 50), g.getFont().getSize()+20, g.getFont().getSize() * 6);

            }
            if (typedPass.length() > 50) {
                if(typed<100)
                    g.drawString(typedPass.substring(50, typed), g.getFont().getSize()+20, g.getFont().getSize() * 8);
                else
                    g.drawString(typedPass.substring(50, 100), g.getFont().getSize()+20, g.getFont().getSize() * 8);
                    
            }
            if (typedPass.length() > 100) 
                g.drawString(typedPass.substring(100, typed), g.getFont().getSize()+20, g.getFont().getSize() * 10);      
            running = false;
        } else if (ended) {
            timer.stop();
            if (wpm <= 40)
                message = "You are an average typist";
            else if (wpm <= 60)
                message = "You are a good typist";
            else if (wpm <= 100)
                message = "You are an excellent typist";
            else
                message = "You are an elite typist";
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.setColor(new Color(176, 165, 132));
            g.drawString("Typing test completed", (SCREEN_WIDTH - metrics.stringWidth("Typing test completed")) / 2,
                    g.getFont().getSize() * 4);
            g.setColor(new Color(24, 161, 127));
            g.drawString("Typing speed : " + wpm + " Words per Minute", (SCREEN_WIDTH - metrics.stringWidth(
                    "Typing speed : " + wpm
                            + " Words per Minute"))
                    / 2,
                    g.getFont().getSize() * 6);
            g.drawString("Typing speed : " + cpm + " Characters per Minute", (SCREEN_WIDTH - metrics.stringWidth(
                "Typing speed : " + cpm
                        + " Characters per Minute"))
                / 2,
                g.getFont().getSize() * 8);
            g.drawString(String.format("Accuracy : %.2f%%", acc), (SCREEN_WIDTH - metrics.stringWidth(
                    String.format("Accuracy : %.2f%%", acc)))
                    / 2,
                    g.getFont().getSize() * 10);
            g.drawString(message, (SCREEN_WIDTH - metrics.stringWidth(
                    message)) / 2,
                    g.getFont().getSize() * 1);
           
            ended = false;
        }
    }
}