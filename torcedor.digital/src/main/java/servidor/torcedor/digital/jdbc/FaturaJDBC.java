package servidor.torcedor.digital.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.google.common.collect.Lists;

import servidor.torcedor.digital.models.Faturamento;

public class FaturaJDBC {

	private Connection conn;

	public FaturaJDBC() {
		try {
			this.conn = new SimpleJdbcConection().getCurrentConection();
		} catch (SQLException e) {

			e.printStackTrace();

			try {
				if (!this.conn.isClosed()) {
					this.conn.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.conn = null;
		}
	}

	public List<Faturamento> listFaturamento() {

		Statement stmt = null;
		List<Faturamento> lista = Lists.newArrayList();

		try {
			stmt = this.conn.createStatement();
			String sql;
			sql = "SELECT * FROM cartao_faturamento WHERE status='concluido'";
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				Faturamento cartaFatura = new Faturamento();
				cartaFatura.setDataCriacao(rs.getTimestamp("data_criacao"));
				cartaFatura.setIdUsuario(rs.getLong("id_usuario"));

				lista.add(cartaFatura);

			}

			rs.close();
			stmt.close();
			this.conn.close();
		} catch (SQLException se) {

			se.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return lista;

	}

}
