package com.rabobank.processer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rabobank.exception.StatementParserExcepion;
import com.rabobank.model.StatementData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlProcesser implements StatementProcesser {

	private MultipartFile xmlFile;

	public XmlProcesser(MultipartFile file) {
		this.xmlFile = file;
	}

	@Override
	public List<StatementData> parseFile() {
		List<StatementData> statements = new ArrayList<>();
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(this.xmlFile.getInputStream());
			doc.getDocumentElement().normalize();

			NodeList nodes = doc.getDocumentElement().getChildNodes();

			IntStream.range(0, nodes.getLength()).forEach(indexNum -> {
				if (nodes.item(indexNum).getNodeType() == Node.ELEMENT_NODE) {
					StatementData statement = new StatementData();

					NodeList childNodes = nodes.item(indexNum).getChildNodes();

					statement.setTransactionRefrence(Integer.parseInt(nodes.item(indexNum).getAttributes().item(0).getNodeValue()));
					if (childNodes.getLength() >= 9) {
						statement.setAccountNumber(childNodes.item(1).getTextContent());
						statement.setDescription(childNodes.item(3).getTextContent());
						statement.setStartBalance(Double.parseDouble(childNodes.item(5).getTextContent()));
						statement.setMutation(Double.parseDouble(childNodes.item(7).getTextContent()));
						statement.setEndBalance(Double.parseDouble(childNodes.item(9).getTextContent()));
					}
					statements.add(statement);
				}
			});
		} catch (Exception ex) {
			log.error("Error while parsing data from XML : " + "Uploaded file : " + this.xmlFile.getOriginalFilename()
					+ " : " + ex.getMessage(), ex);
			throw new StatementParserExcepion("Error while parsing data from XML : " + "Uploaded file : "
					+ this.xmlFile.getOriginalFilename() + " : " + ex.getMessage(), ex);
		}
		return statements;
	}

}
