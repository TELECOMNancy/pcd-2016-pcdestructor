package codingweek2016;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import codingweek2016.features.Model;

@SuppressWarnings("serial")
public class View extends JPanel implements Observer {
	
	private static final long NUMBER_OF_VIDEOS_RETURNED = 3;
	
	private Model model;
	private List<SearchResult> videos = new ArrayList<SearchResult>();
	
	private JButton searchButton = new JButton("Search");
	private JTextField searchField = new JTextField();
	//private JTextArea resultArea = new JTextArea();
	private JEditorPane resultPane = new JEditorPane();
	
	public View(Model m) {
		
		model = m;
		model.addObserver(this);
		
		this.setLayout(new BorderLayout());
		
		searchField.setPreferredSize(new Dimension(300,25));
		
		searchButton.addActionListener(new ActionListener() {
			  
            public void actionPerformed(ActionEvent e) {
            	String text = searchField.getText();
            	model.searchKeyWord(text);

            	if (videos != null) {
            		String result = "<html><head></head><body>";
            		if (!videos.iterator().hasNext()) {
            			result += "<p> There aren't any results for your query.</p>";//resultArea.append(" There aren't any results for your query.");
            		} else {
            			result += "<p><br/>=============================================================<br/>";//resultArea.append("\n=============================================================\n");
            			result += "First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + text + "\".<br/>";//resultArea.append("First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + text + "\".\n"); 
            			result += "=============================================================<br/></p>";//resultArea.append("=============================================================\n");
            		}
            		
    		        //while (videos.iterator().hasNext()) {
            		for (int i=0;i<videos.size();i++) {
	    		            SearchResult singleVideo = videos.get(i);//videos.iterator().next();
	    		            ResourceId rId = singleVideo.getId();
	
	    		            if (rId.getKind().equals("youtube#video")) {
	    		                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
	
	    		                //result += "<p> Video Id: " + rId.getVideoId()+"<br/>";//resultArea.append(" Video Id" + rId.getVideoId());
	    		                result += " Title: " + singleVideo.getSnippet().getTitle()+"<br/>";//resultArea.append(" Title: " + singleVideo.getSnippet().getTitle());
	    		                result += "<img src=" + thumbnail.getUrl()+" width=50 height=50></img><br/>";//resultArea.append(" Thumbnail: " + thumbnail.getUrl());
	    		                result += "<br/>-------------------------------------------------------------<br/></p>";//resultArea.append("\n-------------------------------------------------------------\n");
	    		            }
    		        }
            		result += "</body></html>";
            		resultPane.setContentType("text/html");
            		resultPane.setText(result);
                }
            }
        });
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout());
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		
		this.add(searchPanel, BorderLayout.NORTH);
		
		this.add(resultPane, BorderLayout.CENTER);
		//this.add(resultArea, BorderLayout.CENTER);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable observable, Object videos) {
		if (videos instanceof List<?>) {
			this.videos = (List<SearchResult>) videos;
		}
	}

}