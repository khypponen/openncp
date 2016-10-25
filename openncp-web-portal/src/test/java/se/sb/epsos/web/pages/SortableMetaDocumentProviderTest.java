package se.sb.epsos.web.pages;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

import se.sb.epsos.shelob.ws.client.jaxws.EpsosDocument;
import se.sb.epsos.web.model.LoadableDocumentModel;
import se.sb.epsos.web.service.MetaDocument;

public class SortableMetaDocumentProviderTest {

	@Test
	public void testEmptyList() throws Exception {
		List<LoadableDocumentModel<MetaDocument>> wrappedDocumentList = new ArrayList<LoadableDocumentModel<MetaDocument>>();
		
		SortableMetaDocumentProvider smdp = new SortableMetaDocumentProvider(wrappedDocumentList);

		Iterator<MetaDocument> it = smdp.iterator(0, smdp.size());
		try {
			it.next();
			assertTrue(false);
		} catch(NoSuchElementException e) {
			// Expected
		}
	}

	@Test
	public void testSort() throws Exception {
		EpsosDocument epDocA = new EpsosDocument();
		epDocA.setDescription("A");
		MetaDocument metaDocA = new MetaDocument("SessionId", "PatientId", epDocA);
		LoadableDocumentModel<MetaDocument> loadableA = new LoadableDocumentModel<MetaDocument>(metaDocA);
		

		EpsosDocument epDocB = new EpsosDocument();
		epDocB.setDescription("B");
		MetaDocument metaDocB = new MetaDocument("SessionId", "PatientId", epDocB);
		LoadableDocumentModel<MetaDocument> loadableB = new LoadableDocumentModel<MetaDocument>(metaDocB);

		EpsosDocument epDocC = new EpsosDocument();
		epDocC.setDescription("C");
		MetaDocument metaDocC = new MetaDocument("SessionId", "PatientId", epDocC);
		LoadableDocumentModel<MetaDocument> loadableC = new LoadableDocumentModel<MetaDocument>(metaDocC);

		List<LoadableDocumentModel<MetaDocument>> wrappedDocumentList = new ArrayList<LoadableDocumentModel<MetaDocument>>();
		wrappedDocumentList.add(loadableB);
		wrappedDocumentList.add(loadableC);
		wrappedDocumentList.add(loadableA);
		
		SortableMetaDocumentProvider smdp = new SortableMetaDocumentProvider(wrappedDocumentList);
		smdp.setSort("description", true);
		assertEquals(3, smdp.size());
		
		Iterator<MetaDocument> it = smdp.iterator(0, smdp.size());
		MetaDocument md = it.next();
		assertNotNull(md);
		assertEquals("A", md.getDescription()); 

		md = it.next();
		assertNotNull(md);
		assertEquals("B", md.getDescription()); 

		md = it.next();
		assertNotNull(md);
		assertEquals("C", md.getDescription()); 
	}

}
