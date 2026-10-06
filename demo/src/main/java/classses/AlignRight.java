package classses;

public class AlignRight implements AlignStrategy {
    @Override
    public void render(Paragraph paragraph, Context context) {
        String text = paragraph.getText();
        int spaces = Math.max(0, context.getLineWidth() - text.length());
        System.out.println(" ".repeat(spaces) + text);
    }
}