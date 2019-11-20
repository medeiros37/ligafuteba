package br.com.futeba.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "jogos")
public class Game implements Serializable {

    private static final long serialVersionUID = 2531069826794464004L;

    @Id
    @Column(name = "jogo_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Timestamp data;

    private Timestamp hora;
    
    private Integer quadro;

    @ManyToOne
    @JoinColumn(name = "estabelecimento_id", referencedColumnName = "estabelecimento_id")
    private Place local;

    @ManyToOne(optional = true)
    @JoinColumn(name = "equipe_mandante_id", referencedColumnName = "equipe_id")
    private Team equipeMandante;

    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private Set<GamePlayerData> gamePlayerData;
    
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private Set<GamePlayerData> golsMandante;
    
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private Set<Assists> assMandante;

    @ManyToOne(optional = true)
    @JoinColumn(name = "equipe_visitante_id", referencedColumnName = "equipe_id")
    private Team equipeVisitante;

    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<GamePlayerData> golsVisitante;
    
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Assists> assVisitante;
    
    private Integer totalGolsMandante;
    
    private Integer totalGolsVisitante;
    
    private Integer pontos;

    public Game() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(final Timestamp data) {
        this.data = data;
    }

    public Timestamp getHora() {
        return hora;
    }

    public void setHora(final Timestamp hora) {
        this.hora = hora;
    }

    public Place getLocal() {
        return local;
    }

    public void setLocal(final Place local) {
        this.local = local;
    }

    public Team getHomeTeam() {
        return equipeMandante;
    }

    public void setHomeTeam(final Team equipeMandante) {
        this.equipeMandante = equipeMandante;
    }

    public Team getAwayTeam() {
        return equipeVisitante;
    }

    public void setAwayTeam(final Team equipeVisitante) {
        this.equipeVisitante = equipeVisitante;
    }

    public Set<GamePlayerData> getGamePlayerData() {
        return gamePlayerData;
    }

    public void setGamePlayerData(final Set<GamePlayerData> gamePlayerData) {
        this.gamePlayerData = gamePlayerData;
    }

    public Set<GamePlayerData> getAwayGoals() {
        return golsVisitante;
    }

    public void setAwayGoals(final Set<GamePlayerData> golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

	public Integer getQuadro() {
		return quadro;
	}

	public void setQuadro(Integer quadro) {
		this.quadro = quadro;
	}

	public Integer getHomeTeamTotalGoals() {
		return totalGolsMandante;
	}

	public void setHomeTeamTotalGoals(Integer totalGolsMandante) {
		this.totalGolsMandante = totalGolsMandante;
	}

	public Integer getAwayTeamTotalGoals() {
		return totalGolsVisitante;
	}

	public void setAwayTeamTotalGoals(Integer totalGolsVisitante) {
		this.totalGolsVisitante = totalGolsVisitante;
	}

	public Integer getPoints() {
		return pontos;
	}

	public void setPoints(Integer pontos) {
		this.pontos = pontos;
	}

	public Set<Assists> getHomeAssists() {
		return assMandante;
	}

	public void setHomeAssists(Set<Assists> assMandante) {
		this.assMandante = assMandante;
	}

	public Set<Assists> getAwayAssists() {
		return assVisitante;
	}

	public void setAwayAssists(Set<Assists> assVisitante) {
		this.assVisitante = assVisitante;
	}

	public Set<GamePlayerData> getGolsMandante() {
		return golsMandante;
	}

	public void setGolsMandante(Set<GamePlayerData> golsMandante) {
		this.golsMandante = golsMandante;
	}
}
