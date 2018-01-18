package br.com.ligafuteba.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.ligafuteba.models.Atleta;

public interface AtletaRepository extends JpaRepository<Atleta, Integer> {

    public Atleta findById(@Param("idParam") Integer id);
    
    @Query(value = "SELECT nome, gols, assists , freq FROM atletas " +
    				"INNER JOIN (SELECT atleta_id, SUM(qtd_gols) AS gols FROM gols WHERE gol_id IN (SELECT gols_mandante_gol_id FROM jogos_gols_mandante) GROUP BY atleta_id) a USING (atleta_id) " +
    				"INNER JOIN (SELECT atleta_id, SUM(qtd_assist) AS assists FROM assistencias WHERE assist_id IN (SELECT ass_mandante_assist_id FROM jogos_ass_mandante) GROUP BY atleta_id) b USING (atleta_id) " +
					"INNER JOIN (SELECT atleta_id, (SUM(presence) / (SELECT COUNT(*) FROM jogos WHERE YEAR(data) = 2018)) AS freq FROM gols WHERE gol_id IN (SELECT game_player_data_gol_id FROM jogos_game_player_data) GROUP BY atleta_id) c USING (atleta_id) " +
					"WHERE atleta_id IN (SELECT gols.atleta_id FROM jogos AS jg, jogos_gols_mandante AS gols_mandante,  gols AS gols WHERE YEAR(jg.data) = 2018 AND jg.jogo_id = gols_mandante.jogos_jogo_id AND gols_mandante.gols_mandante_gol_id = gols.gol_id) " +
					"ORDER BY gols DESC, assists DESC, nome ASC", nativeQuery = true)
    public List<?> getEstatisticasAtleta();
    
}
