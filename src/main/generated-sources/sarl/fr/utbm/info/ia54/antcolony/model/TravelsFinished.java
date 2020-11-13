package fr.utbm.info.ia54.antcolony.model;

import fr.utbm.info.ia54.antcolony.model.Road;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Event;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.10")
@SarlElementType(15)
@SuppressWarnings("all")
public class TravelsFinished extends Event {
  public final List<Road> pathTaken;
  
  public final Long timeTaken;
  
  public TravelsFinished(final List<Road> pathTaken, final Long timeTaken) {
    this.pathTaken = pathTaken;
    this.timeTaken = timeTaken;
  }
  
  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TravelsFinished other = (TravelsFinished) obj;
    if (other.timeTaken != this.timeTaken)
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + (int) (this.timeTaken ^ (this.timeTaken >>> 32));
    return result;
  }
  
  /**
   * Returns a String representation of the TravelsFinished event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("pathTaken", this.pathTaken);
    builder.add("timeTaken", this.timeTaken);
  }
  
  @SyntheticMember
  private static final long serialVersionUID = -827891703L;
}
