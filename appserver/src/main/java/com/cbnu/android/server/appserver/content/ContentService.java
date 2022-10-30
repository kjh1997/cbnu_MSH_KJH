package com.cbnu.android.server.appserver.content;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final EntityManager em;

    public String save(Content content1) {
        Content save = contentRepository.save(content1);
        if (save.getBoardidx() == content1.getBoardidx()) {
            return "success";
        }
        return "fail";
    }

    public List<Content> getContentList(String page_num, String content_board_idx) {
        int startIndex = (Integer.parseInt(page_num) - 1) * 10;

        if (Integer.parseInt(content_board_idx) == 0) {
            String sql = "select c from Content c inner join c.writeridx w where c.id >= :a and" +
                    " c.id < :b order by c.id desc";
            TypedQuery<Content> content = em.createQuery(sql, Content.class).setParameter("a",Integer.parseInt(page_num)).setParameter("b", Integer.parseInt(page_num) + 10);
            List<Content> contentList = content.getResultList();
            System.out.println(contentList.size());
            return contentList;
        } else {
            String sql = "select c from Content c inner join c.writeridx w where c.id >= :a and c.id < :b and c.boardidx = :boardidx order by c.id desc ";
            TypedQuery<Content> content = em.createQuery(sql, Content.class)
                    .setParameter("a",Integer.parseInt(page_num))
                    .setParameter("b", Integer.parseInt(page_num) + 10)
                    .setParameter("boardidx",Integer.parseInt(content_board_idx));
            List<Content> contentList = content.getResultList();

            return contentList;
        }

    }

    public Content getContent(String read_content_idx) {
        String sql = "select c from Content c inner join c.writeridx where c.id = :idx";
        TypedQuery<Content> content = em.createQuery(sql, Content.class)
                .setParameter("idx",Integer.parseInt(read_content_idx));
        Content content1 = content.getSingleResult();
        return content1;
    }

    public void deleteContent(String content_idx) {
        Content content = getContent(content_idx);
        contentRepository.delete(content);
    }
}
